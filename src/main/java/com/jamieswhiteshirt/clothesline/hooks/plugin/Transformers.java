package com.jamieswhiteshirt.clothesline.hooks.plugin;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.Map;

public class Transformers {
    public static final Map<String, BytesTransformer> transformers = new HashMap<>();

    static {
        /*
         * This mod disallows placement of blocks that intersect with clotheslines.
         * This hook is needed to be able to properly prevent placement of blocks that intersect with clotheslines.
         * BlockEvent.PlaceEvent only runs on the server, and therefore results in an uncanny interaction where
         * block is visible until the server responds.
         * As a bonus, this ensures that falling blocks will never land in a spot intersecting with a clothesline.
         * Adding unreplaceable blocks intersecting with the clotheslines would be extremely fragile.
         */
        transformers.put("net.minecraft.world.World", singleMethodTransformer(
            "net/minecraft/world/World",
            "func_190527_a", // mayPlace
            "(Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/Entity;)Z",
            methodNode -> {
                InsnList preInstructions = new InsnList();
                preInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                preInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                preInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                preInstructions.add(new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "com/jamieswhiteshirt/clothesline/hooks/CommonHooks",
                    "onMayPlace",
                    "(Lnet/minecraft/world/World;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)Z",
                    false
                ));
                LabelNode labelNode = new LabelNode();
                preInstructions.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
                preInstructions.add(new InsnNode(Opcodes.ICONST_0));
                preInstructions.add(new InsnNode(Opcodes.IRETURN));
                preInstructions.add(labelNode);

                methodNode.instructions.insert(preInstructions);
            }
        ));

        /*
         * Traditionally, the object the mouse is over may only be a block, an entity, or nothing. This mod allows
         * it to be a clothesline or an attachment as well.
         * This hook is needed to reliably insert the potential clothesline or attachment the mouse may be over.
         */
        transformers.put("net.minecraft.client.renderer.EntityRenderer", singleMethodTransformer(
            "net/minecraft/client/renderer/EntityRenderer",
            "func_78473_a", // getMouseOver
            "(F)V",
            methodNode -> {
                boolean success = false;
                for (int i = 0; i < methodNode.instructions.size(); i++) {
                    AbstractInsnNode insnNode = methodNode.instructions.get(i);
                    if (insnNode.getOpcode() == Opcodes.RETURN) {
                        InsnList insnList = new InsnList();
                        insnList.add(new VarInsnNode(Opcodes.FLOAD, 1));
                        insnList.add(new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "com/jamieswhiteshirt/clothesline/hooks/ClientHooks",
                            "onGetMouseOver",
                            "(F)V",
                            false
                        ));
                        i += insnList.size();
                        methodNode.instructions.insertBefore(insnNode, insnList);
                        success = true;
                    }
                }

                if (!success) throw new TransformException("No match found");
            }
        ));

        /*
         * Clotheslines are neither Entities or TileEntities, but they are very much like them. Clotheslines
         * should therefore be rendered around the time Entities and TileEntities are rendered. This should increase
         * compatibility and also makes outline rendering possible.
         * This hook allows rendering things right after tile entities are rendered.
         */
        transformers.put("net.minecraft.client.renderer.RenderGlobal", singleMethodTransformer(
            "net/minecraft/client/renderer/RenderGlobal",
            "func_180446_a", // renderEntities
            "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V",
            methodNode -> {
                for (int i = 0; i < methodNode.instructions.size(); i++) {
                    AbstractInsnNode insnNode = methodNode.instructions.get(i);
                    if (insnNode instanceof MethodInsnNode) {
                        MethodInsnNode mInsnNode = (MethodInsnNode) insnNode;
                        if (
                            mInsnNode.getOpcode() == Opcodes.INVOKESPECIAL &&
                                matches(mInsnNode, "net/minecraft/client/renderer/RenderGlobal", "func_180443_s", "()V") // preRenderDamagedBlocks
                        ) {
                            InsnList insnList = new InsnList();
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            insnList.add(new VarInsnNode(Opcodes.FLOAD, 3));
                            insnList.add(new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "com/jamieswhiteshirt/clothesline/hooks/ClientHooks",
                                "onRenderEntities",
                                "(Lnet/minecraft/client/renderer/culling/ICamera;F)V",
                                false
                            ));
                            i += insnList.size();
                            methodNode.instructions.insertBefore(insnNode, insnList);
                            return;
                        }
                    }
                }

                throw new TransformException("No match found");
            }
        ));

        /*
         * When the client stops using an item, it will only tell the server that it stopped using the item, but not
         * what block the item is being used on at the moment.
         * The server could raytrace it like raytracing is mirrored for buckets, but this can result in
         * desynchronizations. In addition, the client's raytrace sees more objects such as entities, clotheslines
         * and attachments which should not be done on the server.
         * This hook allows intercepting the interaction to allow the client to provide the position of this block.
         * This mirrors how the client tells the server which block it is interacting in onItemUse.
         */
        transformers.put("net.minecraft.client.multiplayer.PlayerControllerMP", singleMethodTransformer(
            "net/minecraft/client/multiplayer/PlayerControllerMP",
            "func_78766_c", // onStoppedUsingItem
            "(Lnet/minecraft/entity/player/EntityPlayer;)V",
            methodNode -> {
                for (int i = 0; i < methodNode.instructions.size(); i++) {
                    AbstractInsnNode insnNode = methodNode.instructions.get(i);
                    if (insnNode instanceof MethodInsnNode) {
                        MethodInsnNode mInsnNode = (MethodInsnNode) insnNode;
                        if (
                            mInsnNode.getOpcode() == Opcodes.INVOKESPECIAL &&
                                matches(mInsnNode, "net/minecraft/client/multiplayer/PlayerControllerMP", "func_78750_j", "()V") // syncCurrentPlayItem
                        ) {
                            InsnList insnList = new InsnList();
                            insnList.add(new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "com/jamieswhiteshirt/clothesline/hooks/ClientHooks",
                                "onStoppedUsingItem",
                                "()Z",
                                false
                            ));
                            LabelNode continueLabel = new LabelNode();
                            insnList.add(new JumpInsnNode(Opcodes.IFEQ, continueLabel));
                            insnList.add(new InsnNode(Opcodes.RETURN));
                            insnList.add(continueLabel);
                            i += insnList.size();
                            methodNode.instructions.insert(insnNode, insnList);
                            return;
                        }
                    }
                }

                throw new TransformException("No match found");
            }
        ));

        /*
         * Players slow down and can't sprint when using items. Clotheslines are susceptible to this behavior in the
         * way their interaction works.
         * This hooks adds a reliable way to bypass the slowdown for specific items.
         */
        transformers.put("net.minecraft.client.entity.EntityPlayerSP", singleMethodTransformer(
            "net/minecraft/client/entity/EntityPlayerSP",
            "func_70636_d", // onLivingUpdate
            "()V",
            methodNode -> {
                for (int i = 0; i < methodNode.instructions.size(); i++) {
                    AbstractInsnNode insnNode = methodNode.instructions.get(i);
                    if (insnNode instanceof MethodInsnNode) {
                        MethodInsnNode mInsnNode = (MethodInsnNode) insnNode;
                        if (
                            mInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL &&
                                matches(mInsnNode, "net/minecraft/client/entity/EntityPlayerSP", "func_184587_cr", "()Z") // isHandActive
                        ) {
                            methodNode.instructions.set(insnNode, new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "com/jamieswhiteshirt/clothesline/hooks/ClientHooks",
                                "isUseItemMovementSlowed",
                                "(Lnet/minecraft/client/entity/EntityPlayerSP;)Z",
                                false
                            ));
                            return;
                        }
                    }
                }

                throw new TransformException("No match found");
            }
        ));
    }

    private static boolean matches(MethodInsnNode insnNode, String owner, String srgName, String desc) {
        String name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, srgName, desc);
        return insnNode.owner.equals(owner) && insnNode.name.equals(name) && insnNode.desc.equals(desc);
    }

    private static BytesTransformer classTransformer(ClassNodeTransformer transformer) {
        return (classLoader, basicClass) -> {
            ClassReader reader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, 0);

            transformer.transform(classNode);

            ClassWriter writer = new SafeClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES, classLoader);
            classNode.accept(writer);
            return writer.toByteArray();
        };
    }

    private static BytesTransformer singleMethodTransformer(String owner, String srgName, String desc, MethodNodeTransformer transformer) {
        String name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, srgName, desc);
        return classTransformer(classNode -> {
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals(name) && methodNode.desc.equals(desc)) {
                    try {
                        transformer.transform(methodNode);
                    } catch (TransformException e) {
                        throw new TransformException("Failed to transform method " + name + "(" + desc, e);
                    }
                    return;
                }
            }

            throw new TransformException("Method " + name + "(" + desc + " not found");
        });
    }
}
