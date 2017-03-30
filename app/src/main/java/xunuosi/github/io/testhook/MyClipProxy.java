package xunuosi.github.io.testhook;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by xns on 2017/3/30.
 * hook系统剪切板服务
 * 动态代理,就是在实现阶段不需要制定代理谁，在运行的时候指定一个代理类，这样就更灵活了，主要实现通过Java提供的InvocationHanler类，

 1.写一个类实现InvocationHanlder接口

 2.重写接口的invoke方法

 3.通过调用Proxy.newProxyInstance(ClassLoader loader, Class[] claz , InvocationHanlder handler)返回一个代理对象;
 */

public class MyClipProxy implements InvocationHandler {
    private final IBinder mBase;

    public MyClipProxy(IBinder binder) {
        // 传入系统原有的Binder对象
        mBase = binder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.e("xns", "MyClipProxy.invoke()");
        //拦截原系统类查询本地是否有这个代理的方法
        if ("queryLocalInterface".equals(method.getName())) {
            Log.e("xns", "queryLocalInterface");
            // 创建自己的代理类返回以代替系统的原有代理类
            Class<?> mStubClass = Class.forName("android.content.IClipboard$Stub");
            // 在拿到IClipboard本地对象类
            Class<?> mIClipboard  = Class.forName("android.content.IClipboard");
            // 创建我们自己的代理
            return Proxy.newProxyInstance(mStubClass.getClassLoader(),
                    new Class[]{mIClipboard},
                    new MyClip(mBase, mStubClass));
        }
        // 不是这个方法还是返回原系统的执行
        return method.invoke(mBase, args);
    }
}
