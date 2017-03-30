package xunuosi.github.io.testhook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClipHelper.binder();
        setContentView(R.layout.activity_main);
    }
}
