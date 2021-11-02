# Android--UI之ImageSwitcher

**前言**

　　这篇博客来聊一聊AndroidUI开发中ImageSwitcher控件的使用。ImageSwitcher控件与ImageView类似，都可以用于显示图片，但是ImageSwitcher通过名字可以看出，主要是用于多张图片的切换显示。在本篇博客中，会介绍ImageSwitcher控件的基本属性的设置以及常用方法的调用。在最后会通过一个示例Demo来展示本篇博客中讲到的一些内容。

**ImageSwitcher**

　　[ImageSwitcher](http://developer.android.com/reference/android/widget/ImageSwitcher.html)是一个图片切换器，它间接继承自FrameLayout类，和ImageView相比，多了一个功能，那就是它说显示的图片切换时，可以设置动画效果，类似于淡进淡出效果，以及左进右出滑动等效果。

　　既然ImageSwitcher是用来显示图片的控件，AndroidAPI为我们提供了三种不同方法来设定不同的图像来源，方法有：

*   setImageDrawable(Drawable)：指定一个Drawable对象，用来给ImageSwitcher显示。
*   setImageResource(int)：指定一个资源的ID，用来给ImageSwitcher显示。
*   setImageURL(URL)：指定一个URL地址，用来给ImageSwitcher显示URL指向的图片资源。

**动画效果设定**

　　上面介绍到，ImageSwitcher可以设置图片切换时，动画的效果。对于动画效果的支持，是因为它继承了ViewAnimator类，这个类中定义了两个属性，用来确定切入图片的动画效果和切出图片的动画效果：

*   android:inAnimation：切入图片时的效果。
*   android:outAnimation：切出图片时的效果。

　　以上两个属性如果在XML中设定的话，当然可以通过XML资源文件自定义动画效果，但是如果只是想使用Android自带的一些简单的效果的话，需要设置参数为“@android:anim/AnimName”来设定效果，其中AnimName为指定的动画效果。如果在代码中设定的话，可以直接使用setInAnimation()和setOutAnimation()方法。它们都传递一个Animation的抽象对象，Animation用于描述一个动画效果，一般使用一个AnimationUtils的工具类获得。对于动画效果，不是本片博客的重点，关于Android的动画效果，以后再详细讲解。

　　对于动画效果，一般定义在[android.R.anim](http://developer.android.com/reference/android/R.anim.html)类中，它是一个final类，以一些int常量的形式，定义的样式，这里仅仅介绍两组样式，淡进淡出效果，以及左进右出滑动效果，如果需要其他效果，可以查阅官方文档。

*   fede\_in：淡进。
*   fade\_out:淡出
*   slide\_in\_left:从左滑进。
*   slide\_out\_right: 从右滑出。

　　一般使用的话，通过这些常量名称就可以看出是什么效果，这里并不是强制Xxx\_in\_Xxx就一定对应了setInAnimation()方法，但是一般如果不成组设定的话，效果会很丑，建议还是成组的对应In和Out设定效果。

**ViewFactory**

　　在使用ImageSwitcher的时候，有一点特别需要注意的，需要通过setFactory()方法为它设置一个ViewSwitcher.ViewFactory接口，设置这个ViewFactory接口时需要实现makeView()方法，该方法通常会返回一个ImageView，而ImageSwitcher则负责显示这个ImageView。如果不设定ViewFactory的话，ImageSwitcher将无法使用。通过官方文档了解到，setFactory()方法被声明在ViewSwitcher类中，而ImageSwitcher直接继承自ViewSwitcher类。ViewSwitcher的功能与ImageSwitcher类似，只是ImageSwitcher用于展示图片，而ViewSwitcher用于展示一些View视图。

　　可以这么理解，通过ViewFactory中的makeView()方法返回一个新的View视图，用来放入ViewSwitcher中展示，而对于ImageSwitcher而言，这里通常返回的是一个ImageView。

**示例程序**

　　下面通过一个Demo来说明上面讲到的内容。在示例中定义一个ImageSwitcher和两个Button，这两个按钮分别控制着图像的上一张、下一张显示，当然，在资源中必须存在这几个待切换的图片文件。。

　　布局代码：

```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context=".MainActivity" android:orientation="vertical">

    <ImageSwitcher
        android:id="@+id/imageSwitcher1"
        android:layout_width="fill_parent"
        android:layout_height="150dp"
         />
    <Button
        android:id="@+id/btnadd"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="上一张" />
    <Button
        android:id="@+id/btnSub"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="下一张" />
</LinearLayout>
```
　　实现代码：

```
public class MainActivity extends Activity {
    private Button btnAdd, btnSub;
    private ImageSwitcher imageSwitcher;
    private int index = 0;
    private List<Drawable> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        putData();
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        btnAdd = (Button) findViewById(R.id.btnadd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnAdd.setOnClickListener(myClick);
        btnSub.setOnClickListener(myClick);

        //通过代码设定从左缓进，从右换出的效果。
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right));
        imageSwitcher.setFactory(new ViewFactory() {

            @Override
            public View makeView() {
                // makeView返回的是当前需要显示的ImageView控件，用于填充进ImageSwitcher中。
                return new ImageView(MainActivity.this);
            }
        });
        imageSwitcher.setImageDrawable(list.get(0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private View.OnClickListener myClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btnadd:
                index--;
                if(index<0)
                {
                    //用于循环显示图片
                    index=list.size()-1;
                }
                //设定ImageSwitcher显示新图片
                imageSwitcher.setImageDrawable(list.get(index));
                break;

            case R.id.btnSub:
                index++;
                if(index>=list.size())
                {
                    //用于循环显示图片
                    index=0;
                }
                imageSwitcher.setImageDrawable(list.get(index));
                break;
            }
        }
    };

    private void putData() {
        //填充图片的Drawable资源数组
        list = new ArrayList<Drawable>();
        list.add(getResources().getDrawable(R.drawable.bmp1));
        list.add(getResources().getDrawable(R.drawable.bmp2));
        list.add(getResources().getDrawable(R.drawable.bmp3));
        list.add(getResources().getDrawable(R.drawable.bmp4));
        list.add(getResources().getDrawable(R.drawable.bmp5));
    }
}
```

# Android ViewSwitcher简介和使用
**Android ViewSwitcher简介和使用**

ViewSwitcher
ViewSwitcher顾名思义. ViewSwitcher主要应用场景之一：比如在一个布局文件中，根据业务需求，需要在两个View间切换，在任意一个时刻，只能显示一个View.

ViewSwitcher本身继承了 FrameLayout,因此可以将多个View 层叠在一起，每次只显示一个组件。当程序控制从一个View切换到另一个View时， ViewSwitcher支持指定动画效果



Android ViewSwitcher主要应用场景之一：比如在一个布局文件中，根据业务需求，需要在两个View间切换，在任意一个时刻，只能显示一个View。
典型的应用比如一些社交类APP的标题栏，在分享照片之前，标题栏显示“拍照”按钮，用户拍完照后，接下来的动作是发送这张照片，那么合理的做法就是将标题栏的动作按钮变成“发送”按钮。
针对此种情况，相对比较简陋的做法是：可以考虑在标题栏事先存放两个按钮：拍照，发送。当前是“拍照”时，则将“发送”按钮设置为GONE；反之，当前是“发送”时，则将“拍照”按钮设置为GONE。
但是，相对更好的做法是使用ViewSwitcher。ViewSwitcher，故名思议，就是为了完成View之间的Switch。


# Android ViewSwitcher 的使用
## ViewSwitcher

ViewSwitcher 代表了视图切换组件, 本身继承了FrameLayout ,可以将多个View叠在一起 ,每次只显示一个组件.当程序控制从一个View切换到另个View时,ViewSwitcher 支持指定动画效果。

ViewAnimator是一个基类，它继承了 FrameLayout，因此它表现出FrameLayout的特征，可以将多个View组件叠在一起。 ViewAnimator额外增加的功能正如它的名字所暗示的一样，ViewAnimator可以在View切换时表现出动画效果。

iewAnimator及其子类的继承关系图如下图所示：

![](https://upload-images.jianshu.io/upload_images/3584280-53c2a007e9466457.png)

image.png

**ViewAnimator**
ViewAnimator及其子类也是一组非常重要的UI组件，这种组件的主要功能是增加动画效果，从而使界面更加炫。使用ViewAnimator 时可以指定如下常见XML属性。

*   android:animateFirstView：设置ViewAnimator显示第一个View组件时是否使用动画。
*   android:inAnimation：设置ViewAnimator显示组件时所使用的动画。
*   android:outAnimation：设置ViewAnimator隐藏组件时所使用的动画。

ViewSwitcher继承ViewAnimator，主要用于视图的切换：

```java
public class ViewSwitcher extends ViewAnimator {

}

```

ViewSwitcher重写了addView(View, int, ViewGroup.LayoutParams)方法，使其子控件不超过2个：

```java
   /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if this switcher already contains two children
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("Can't add more than 2 views to a ViewSwitcher");
        }
        super.addView(child, index, params);
    }

```

通过配置属性指定切换动画：

1.  android:inAnimation指定进入时动画
2.  android:outAnimation指定退出时动画

> 调用ViewSwitcher的showNext()和showPrevious()来实现视图的切换。

**setFactory设置视图**

ViewSwitcher中setFactory(ViewFactory)方法设置了子视图，调用obtainView()方法添加了两个子控件。

```java
public void setFactory(ViewFactory factory) {
    mFactory = factory;
    obtainView();
    obtainView();
}

private View obtainView() {
    View child = mFactory.makeView();
    LayoutParams lp = (LayoutParams) child.getLayoutParams();
    if (lp == null) {
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }
    addView(child, lp);
    return child;
}

    public interface ViewFactory {
        /**
         * Creates a new {@link android.view.View} to be added in a
         * {@link android.widget.ViewSwitcher}.
         *
         * @return a {@link android.view.View}
         */
        View makeView();
    }

```

## ViewSwitcher的使用

切换图片案例：

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".activity.ViewSwitcherDemoActivity">

        <!-- ViewSwitcher子控件不能超过2个 -->
        <ViewSwitcher
            android:id="@+id/view_switcher"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inAnimation="@anim/anim_enter_from_bottom"
            android:outAnimation="@anim/anim_exit_to_top"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="200dp"
                android:scaleType="fitXY"
                android:src="@drawable/pic1" />

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="200dp"
                android:scaleType="fitXY"
                android:src="@drawable/pic2" />

        </ViewSwitcher>

        <androidx.appcompat.widget.AppCompatButton
            android:id="@+id/btn_next"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="showNext"
            android:textAllCaps="false"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toBottomOf="@id/view_switcher" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>

```

```java
public class ViewSwitcherDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_switcher_demo);

        ActivityViewSwitcherDemoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_view_switcher_demo);

        binding.btnNext.setOnClickListener(v -> {
            binding.viewSwitcher.showNext();
        });
    }
}

```

进入动画anim\_enter\_from\_bottom.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">

    <translate
        android:duration="1000"
        android:fromYDelta="100%p"
        android:toYDelta="0%" />

</set>

```

退出动画anim\_exit\_to\_top.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<translate xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromYDelta="0"
    android:toYDelta="-100%"
    android:duration="1000" />

```

**动态给ViewSwitcher添加子View**

```cpp
       //动态给ViewSwitcher添加子View
        binding.viewSwitcher.setFactory(() -> {
            ImageView iv = new ImageView(ViewSwitcherDemoActivity.this);
            iv.setImageResource(R.drawable.pic1);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(new FrameLayout.LayoutParams(-1, Utils.dip2px(200)));
            return iv;
        });

        binding.btnNext.setOnClickListener(v -> {
            if (binding.viewSwitcher.getDisplayedChild() == 0) {
                ImageView iv = (ImageView) binding.viewSwitcher.getChildAt(1);
                iv.setImageResource(R.drawable.pic2);
                binding.viewSwitcher.showNext();
            } else {
                binding.viewSwitcher.showPrevious();
            }
        });

```

**多个视图切换**

有多个视图需要时，需要自定义next()和previous()方法。

为了给ViewSwitcher 添加多个组件, 一般通过ViewSwitcher 的setFactory 方法为止设置ViewFactory ,并由ViewFactory为之创建View 即可.

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".activity.ViewSwitcherDemoActivity">

        <!-- ViewSwitcher子控件不能超过2个 -->
        <ViewSwitcher
            android:id="@+id/view_switcher"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inAnimation="@anim/anim_enter_from_bottom"
            android:outAnimation="@anim/anim_exit_to_top"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toTopOf="parent">

        </ViewSwitcher>

        <androidx.appcompat.widget.AppCompatButton
            android:id="@+id/btn_next"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="showNext"
            android:textAllCaps="false"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toBottomOf="@id/view_switcher" />

        <androidx.appcompat.widget.AppCompatButton
            android:id="@+id/btn_previous"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="showPrevious"
            android:textAllCaps="false"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toBottomOf="@id/btn_next" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>

```

```java
public class ViewSwitcherDemoActivity extends AppCompatActivity {

    private ActivityViewSwitcherDemoBinding mBinding;

    private int[] mResIdArray = {
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3
    };

    private int mCount = mResIdArray.length;

    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_switcher_demo);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_switcher_demo);

        mBinding.viewSwitcher.setFactory(() -> {
            ImageView iv = new ImageView(ViewSwitcherDemoActivity.this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(mResIdArray[0]);
            iv.setLayoutParams(new FrameLayout.LayoutParams(-1, Utils.dip2px(200)));
            return iv;
        });

        //下一张
        mBinding.btnNext.setOnClickListener(v -> next());

        //上一张
        mBinding.btnPrevious.setOnClickListener(v -> previous());

    }

    private void next() {
        ++mPosition;
        setSelection((ImageView) mBinding.viewSwitcher.getNextView());

        mBinding.viewSwitcher.setInAnimation(this, R.anim.anim_enter_from_bottom);
        mBinding.viewSwitcher.setOutAnimation(this, R.anim.anim_exit_to_top);
        mBinding.viewSwitcher.showNext();
    }

    private void previous() {
        --mPosition;
        setSelection((ImageView) mBinding.viewSwitcher.getNextView());

        mBinding.viewSwitcher.setInAnimation(this, R.anim.anim_enter_from_top);
        mBinding.viewSwitcher.setOutAnimation(this, R.anim.anim_exit_to_bottom);
        mBinding.viewSwitcher.showPrevious();
    }

    private void setSelection(ImageView imageView) {
        if (mPosition < 0) {
            mPosition = mCount - 1;
        } else if (mPosition >= mCount) {
            mPosition = 0;
        }
        imageView.setImageResource(mResIdArray[mPosition]);
    }
}

```

进入动画anim\_enter\_from\_top.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<translate xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromYDelta="-100%"
    android:toYDelta="0"
    android:duration="1000" />

```

退出动画anim\_exit\_to\_bottom.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<translate xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromYDelta="0"
    android:duration="1000"
    android:toYDelta="100%" />

```

## ViewSwitcher实现切换登陆方式界面案例

![](https://upload-images.jianshu.io/upload_images/3584280-748fc297afa8cb76.png)

手机快捷登录

![](https://upload-images.jianshu.io/upload_images/3584280-0830b3622ec5ff0f.png)

账号密码登录

登陆界面布局：

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".activity.ViewSwitcherActivity">

        <TextView
            android:id="@+id/tv_login_title"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="50dp"
            android:gravity="center"
            android:paddingBottom="50dp"
            android:text="登陆"
            android:textSize="20sp"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <ViewSwitcher
            android:id="@+id/vs_input"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="36dp"
            android:animateFirstView="true"
            android:inAnimation="@anim/slide_in_from_left"
            android:outAnimation="@anim/slide_out_to_left"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toBottomOf="@id/tv_login_title">

            <!--手机快捷登录-->
            <include
                android:id="@+id/in_phone_input"
                layout="@layout/login_by_phone_layout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />

            <!--账号密码登陆-->
            <include
                android:id="@+id/in_phone_pwd_input"
                layout="@layout/login_by_pwd_layout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />

        </ViewSwitcher>

        <androidx.appcompat.widget.AppCompatButton
            android:id="@+id/btn_login"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="登陆"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toBottomOf="@id/vs_input" />

        <androidx.appcompat.widget.AppCompatTextView
            android:id="@+id/tv_switch_login_type"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="10dp"
            android:text="账号密码登陆"
            android:textSize="16sp"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toBottomOf="@id/btn_login" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>

```

ViewSwitcherActivity

```java
public class ViewSwitcherActivity extends AppCompatActivity {

    private ActivityViewSwitcherBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_switcher);

        mBinding.tvSwitchLoginType.setOnClickListener(v -> {
            if (mBinding.vsInput.getDisplayedChild() == 0) {
                showPwd();
                mBinding.tvSwitchLoginType.setText("手机快捷登录");
            } else {
                showPhone();
                mBinding.tvSwitchLoginType.setText("账号密码登陆");
            }
        });
    }

    private void showPwd() {
        mBinding.vsInput.setInAnimation(this, R.anim.slide_in_from_right);
        mBinding.vsInput.setOutAnimation(this, R.anim.slide_out_to_left);
        mBinding.vsInput.showNext();

    }

    private void showPhone() {
        mBinding.vsInput.setInAnimation(this, R.anim.slide_in_from_left);
        mBinding.vsInput.setOutAnimation(this, R.anim.slide_out_to_right);
        mBinding.vsInput.showPrevious();
    }
}

```

slide\_in\_from\_right.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">

    <translate
        android:duration="250"
        android:fromXDelta="100%p"
        android:toYDelta="0%p" />

</set>

```

slide\_out\_to\_right.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="250"
        android:fromXDelta="0.0%p"
        android:toXDelta="100%p" />
</set>

```

slide\_in\_from\_left.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="250"
        android:fromXDelta="-100%p"
        android:toXDelta="0.0%p" />
</set>

```

slide\_out\_to\_left.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="250"
        android:fromXDelta="0.0%p"
        android:toXDelta="-100%p" />
</set>

```

## ViewFlipper类

ViewFlipper继承ViewAnimator，用于视图的轮播。

*   android:flipInterval指定轮播间隔时间
*   android:autoStart是否自动开始轮播
*   android:inAnimation指定进入时动画
*   android:outAnimation指定退出时动画

```xml
       <ViewFlipper
            android:id="@+id/view_flipper"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:autoStart="true"
            android:flipInterval="2000"
            android:inAnimation="@anim/slide_in_from_right"
            android:outAnimation="@anim/slide_out_to_left"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:scaleType="fitXY"
                android:src="@drawable/pic1" />

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:scaleType="fitXY"
                android:src="@drawable/pic2" />

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:scaleType="fitXY"
                android:src="@drawable/pic3" />

        </ViewFlipper>

```

主要方法：
startFlipping()用于手动开始轮播，而stopFlipping()则停止轮播。
showNext()和showPrevious()显示视图的切换。

```cpp
// 显示上一个视图
private void previous() {
    mViewFlipper.stopFlipping();

    mViewFlipper.setInAnimation(this, R.anim.anim_enter_from_top);
    mViewFlipper.setOutAnimation(this, R.anim.anim_exit_to_bottom);
    mViewFlipper.showPrevious();
}

// 手动开始轮播
private void play() {
    mViewFlipper.setInAnimation(this, R.anim.anim_enter_from_bottom);
    mViewFlipper.setOutAnimation(this, R.anim.anim_exit_to_top);
    mViewFlipper.startFlipping();
}

// 显示下一个视图
private void next() {
    mViewFlipper.stopFlipping();

    mViewFlipper.setInAnimation(this, R.anim.anim_enter_from_bottom);
    mViewFlipper.setOutAnimation(this, R.anim.anim_exit_to_top);
    mViewFlipper.showNext();
}

```

## TextSwitcher和ImageSwitcher

ImageSwitcher和TextSwitcher的继承关系是一样的。两个重要的父类：ViewSwitcher和ViewAnimator。

继承于ViewSwitcher，说明具备了切换功能，

继承于ViewAnimator，说明具备了动画功能。