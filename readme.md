
# Android ViewSwitcher简介和使用
**Android ViewSwitcher简介和使用**

ViewSwitcher
ViewSwitcher顾名思义. ViewSwitcher主要应用场景之一：比如在一个布局文件中，根据业务需求，需要在两个View间切换，在任意一个时刻，只能显示一个View.

ViewSwitcher本身继承了 FrameLayout,因此可以将多个View 层叠在一起，每次只显示一个组件。当程序控制从一个View切换到另一个View时， ViewSwitcher支持指定动画效果



Android ViewSwitcher主要应用场景之一：比如在一个布局文件中，根据业务需求，需要在两个View间切换，在任意一个时刻，只能显示一个View。
典型的应用比如一些社交类APP的标题栏，在分享照片之前，标题栏显示“拍照”按钮，用户拍完照后，接下来的动作是发送这张照片，那么合理的做法就是将标题栏的动作按钮变成“发送”按钮。
针对此种情况，相对比较简陋的做法是：可以考虑在标题栏事先存放两个按钮：拍照，发送。当前是“拍照”时，则将“发送”按钮设置为GONE；反之，当前是“发送”时，则将“拍照”按钮设置为GONE。
但是，相对更好的做法是使用ViewSwitcher。ViewSwitcher，故名思议，就是为了完成View之间的Switch。
现在给出一个完整示例代码例子说明。

测试的MainActivity.java :

```java
package zhangphil.view; import android.app.Activity;import android.os.Bundle;import android.view.View;import android.view.animation.Animation;import android.view.animation.AnimationUtils;import android.widget.Button;import android.widget.ViewSwitcher; public class MainActivity extends Activity { 	@Override	public void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState); 		setContentView(R.layout.activity_main); 		Button buttonPrev = (Button) findViewById(R.id.prev);		Button buttonNext = (Button) findViewById(R.id.next); 		final ViewSwitcher viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher); 		Animation slide_in_left = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);		Animation slide_out_right = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right); 		viewSwitcher.setInAnimation(slide_in_left);		viewSwitcher.setOutAnimation(slide_out_right); 		buttonPrev.setOnClickListener(new View.OnClickListener() { 			@Override			public void onClick(View v) {				// viewSwitcher.showPrevious();切换效果类似				viewSwitcher.setDisplayedChild(0);			}		}); 		buttonNext.setOnClickListener(new View.OnClickListener() { 			@Override			public void onClick(View v) {				// viewSwitcher.showNext();切换效果类似				viewSwitcher.setDisplayedChild(1);			}		});	}}
```

MainActivity.java需要的布局文件activity\_main.xml :

```html
<?xml version="1.0" encoding="utf-8"?><LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"    android:layout_width="match_parent"    android:layout_height="match_parent"    android:orientation="vertical" >     <LinearLayout        android:layout_width="match_parent"        android:layout_height="wrap_content"        android:orientation="horizontal" >         <Button            android:id="@+id/prev"            android:layout_width="wrap_content"            android:layout_height="wrap_content"            android:text="前" />         <Button            android:id="@+id/next"            android:layout_width="wrap_content"            android:layout_height="wrap_content"            android:text="后" />    </LinearLayout>     <ViewSwitcher        android:id="@+id/viewSwitcher"        android:layout_width="match_parent"        android:layout_height="wrap_content" >         <Button            android:layout_width="wrap_content"            android:layout_height="wrap_content"            android:text="1" />         <LinearLayout            android:layout_width="match_parent"            android:layout_height="wrap_content"            android:orientation="vertical" >             <Button                android:layout_width="wrap_content"                android:layout_height="wrap_content"                android:text="2" />             <TextView                android:layout_width="wrap_content"                android:layout_height="wrap_content"                android:text="线性布局的TextView" />        </LinearLayout>    </ViewSwitcher> </LinearLayout>
```

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