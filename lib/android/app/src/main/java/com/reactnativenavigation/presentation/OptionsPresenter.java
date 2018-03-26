package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reactnativenavigation.interfaces.ChildDisappearListener;
import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.TopBar;

import java.util.ArrayList;

public class OptionsPresenter {
    private TopBar topBar;

    public OptionsPresenter(TopBar topBar) {
        this.topBar = topBar;
    }

    public void applyChildOptions(Options options, Component child) {
        applyOrientation(options.orientationOptions);
        applyButtons(options.topBarOptions.leftButtons, options.topBarOptions.rightButtons);
        applyTopBarOptions(options.topBarOptions, child, options.animationsOptions);
        applyTopTabsOptions(options.topTabsOptions);
        applyTopTabOptions(options.topTabOptions);
    }

    public void applyOrientation(OrientationOptions options) {
        ((Activity) topBar.getContext()).setRequestedOrientation(options.getValue());
    }

    private void applyTopBarOptions(TopBarOptions options, Component component, AnimationsOptions animationOptions) {
        if (options.title.text.hasValue()) topBar.setTitle(options.title.text.get());
        if (options.title.component.hasValue())
            topBar.setComponent(options.title.component.get(), options.title.alignment);
        topBar.setBackgroundColor(options.background.color);
        topBar.setTitleTextColor(options.title.color);
        topBar.setTitleFontSize(options.title.fontSize);
        if (options.testId.hasValue()) topBar.setTestId(options.testId.get());

        topBar.setTitleTypeface(options.title.fontFamily);
        if (options.visible.isFalse()) {
            if (options.animate.isTrueOrUndefined()) {
                topBar.hideAnimate(animationOptions.pop.topBar);
            } else {
                topBar.hide();
            }
        }
        if (options.visible.isTrueOrUndefined()) {
            if (options.animate.isTrueOrUndefined()) {
                topBar.showAnimate(animationOptions.push.topBar);
            } else {
                topBar.show();
            }
        }
        if (options.drawBehind.isTrue()) {
            component.drawBehindTopBar();
        } else if (options.drawBehind.isFalseOrUndefined()) {
            component.drawBelowTopBar(topBar);
        }
        if (options.hideOnScroll.isTrue()) {
            if (component instanceof IReactView) {
                topBar.enableCollapse(((IReactView) component).getScrollEventListener());
            }
        } else if (options.hideOnScroll.isFalseOrUndefined()) {
            topBar.disableCollapse();
        }
    }

    private void applyButtons(ArrayList<Button> leftButtons, ArrayList<Button> rightButtons) {
        topBar.setButtons(leftButtons, rightButtons);
    }

    private void applyTopTabsOptions(TopTabsOptions options) {
        topBar.applyTopTabsColors(options.selectedTabColor, options.unselectedTabColor);
        topBar.applyTopTabsFontSize(options.fontSize);
        topBar.setTopTabsVisible(options.visible.isTrueOrUndefined());
    }

    private void applyTopTabOptions(TopTabOptions topTabOptions) {
        if (topTabOptions.fontFamily != null) {
            topBar.setTopTabFontFamily(topTabOptions.tabIndex, topTabOptions.fontFamily);
        }
    }

    public void onChildWillDisappear(Options disappearing, Options appearing, @NonNull ChildDisappearListener childDisappearListener) {
        if (disappearing.topBarOptions.visible.isTrueOrUndefined() && appearing.topBarOptions.visible.isFalse()) {
            if (disappearing.topBarOptions.animate.isTrueOrUndefined()) {
                topBar.hideAnimate(disappearing.animationsOptions.pop.topBar, childDisappearListener::childDisappear);
            } else {
                topBar.hide();
                childDisappearListener.childDisappear();
            }
        } else {
            childDisappearListener.childDisappear();
        }
    }
}
