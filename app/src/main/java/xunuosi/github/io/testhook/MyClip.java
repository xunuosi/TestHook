package xunuosi.github.io.testhook;

import android.content.ClipData;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xns on 2017/3/30.
 * HOOK 系统剪切板服务
 *
 */

public class MyClip implements InvocationHandler {
    private Object mBase;

    public MyClip(IBinder binder, Class stub) {
        //拿到asInterface方法，因为源码中执行了这一句，我们也要执行这一句
        try {
            Method asInterface = stub.getDeclaredMethod("asInterface", IBinder.class);
            mBase = asInterface.invoke(null, binder);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.e("xns", "MyClip.invoke(),Method:" + method.getName());
        // 我们拦截粘贴的方法
        if ("getPrimaryClip".equals(method.getName())) {
            Log.e("xns", "getPrimaryClip");
            return ClipData.newPlainText(null,"我是徐诺斯，我使用了HOOK");
        }
        // 再拦截是否有复制的方法,让系统认为一直都有
        if ("hasPrimaryClip".equals(method.getName())) {
            Log.e("xns", "hasPrimaryClip");
            return true;
        }
        //其他启动还是返回原有的
        return method.invoke(mBase,args);
    }
}
