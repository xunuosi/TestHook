package xunuosi.github.io.testhook;

import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 动态代理,就是在实现阶段不需要制定代理谁，在运行的时候指定一个代理类，这样就更灵活了，主要实现通过Java提供的InvocationHanler类，

 1.写一个类实现InvocationHanlder接口

 2.重写接口的invoke方法

 3.通过调用Proxy.newProxyInstance(ClassLoader loader, Class[] claz , InvocationHanlder handler)返回一个代理对象;
 */

public class ClipHelper {

    public static void binder() {
        try {
            // 通过反射拿到ServiceManager
            Class<?> serviceManagerClass = Class.forName("android.os.ServiceManager");
            // 拿到getService方法
            Method getServiceMethod = serviceManagerClass.getMethod("getService", String.class);
            // 调用该方法得到系统中的Binder对象
            IBinder binder = (IBinder) getServiceMethod.invoke(null, "clipboard");
            // 通过系统的Binder对象创建自己的代理对象稍后注册到系统的Binder集合中
            IBinder myBinder = (IBinder) Proxy.newProxyInstance(serviceManagerClass.getClassLoader(),
                    new Class[]{IBinder.class},
                    new MyClipProxy(binder));
            // 得到ServiceManager中的维护数组
            Field field = serviceManagerClass.getDeclaredField("sCache");
            // 设置java取消访问检查，也就是说如果是私有的也可以访问
            field.setAccessible(true);
            Map<String,IBinder> map = (Map) field.get(null);
            // 将我们的服务类存入map
            map.put("clipboard", myBinder);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
