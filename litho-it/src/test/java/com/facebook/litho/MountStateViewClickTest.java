/**
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho;

import com.facebook.yoga.YogaAlign;

import com.facebook.yoga.YogaFlexDirection;

import com.facebook.litho.testing.ComponentTestHelper;
import com.facebook.litho.testing.testrunner.ComponentsTestRunner;
import com.facebook.litho.testing.TestDrawableComponent;
import com.facebook.litho.testing.TestViewComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(ComponentsTestRunner.class)
public class MountStateViewClickTest {

  private ComponentContext mContext;

  @Before
  public void setup() {
    mContext = new ComponentContext(RuntimeEnvironment.application);
  }

  @Test
  public void testInnerComponentHostClickable() {
    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(
                    Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                        .clickHandler(c.newEventHandler(1))
                        .child(TestViewComponent.create(c)))
                .build();
          }
        });

    assertEquals(1, componentView.getChildCount());
    assertFalse(componentView.isClickable());

    ComponentHost innerHost = (ComponentHost) componentView.getChildAt(0);
    assertTrue(innerHost.isClickable());
  }

  @Test
  public void testInnerComponentHostClickableWithLongClickHandler() {
    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(
                    Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                        .longClickHandler(c.newEventHandler(1))
                        .child(TestViewComponent.create(c)))
                .build();
          }
        });

    assertEquals(1, componentView.getChildCount());
    assertFalse(componentView.isClickable());

    ComponentHost innerHost = (ComponentHost) componentView.getChildAt(0);
    assertTrue(innerHost.isLongClickable());
  }

  @Test
  public void testRootHostClickable() {
    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .clickHandler(c.newEventHandler(1))
                .child(TestDrawableComponent.create(c))
                .build();
          }
        });

    assertEquals(0, componentView.getChildCount());
    assertTrue(componentView.isClickable());
  }

  @Test
  public void testRootHostClickableWithLongClickHandler() {
    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .longClickHandler(c.newEventHandler(1))
                .child(TestDrawableComponent.create(c))
                .build();
          }
        });

    assertEquals(0, componentView.getChildCount());
    assertTrue(componentView.isLongClickable());
  }
}
