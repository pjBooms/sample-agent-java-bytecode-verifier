package com.excelsior.sample;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class ClassTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
        if (!className.equals("HelloWorld")) {
            //instrument HelloWorld class only
            return null;
        }
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassWriter cw = new ClassWriter(0);
//        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);//uncomment this to fix "Expecting a stackmap frame" VerifyError
        ClassVisitor transformer = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, final String mname, final String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, mname, desc, signature, exceptions);
                if (mname.equals("main")) {
                    //Instrument the body of main method only
                    //Insert some instructions to the beginning of the method:
//                    mv.visitInsn(Opcodes.POP); //Uncomment this to see VerifyError
//                    Label l = new Label(); //Uncomment the next 4 lines to see "Expecting a stackmap frame" VerifyError
//                    mv.visitIntInsn(Opcodes.BIPUSH, 0);
//                    mv.visitJumpInsn(Opcodes.IFNE, l);
//                    mv.visitLabel(l);
                }
                return mv;
            }
        };
        cr.accept(transformer, 0);
        return cw.toByteArray();
    }
}