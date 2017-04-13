/*
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho;

import com.facebook.litho.config.ComponentsConfiguration;
import com.facebook.litho.testing.ComponentTestHelper;
import com.facebook.litho.testing.TestDrawableComponent;
import com.facebook.litho.testing.assertj.ComponentViewAssert;
import com.facebook.litho.testing.testrunner.ComponentsTestRunner;
import com.facebook.litho.testing.util.InlineLayoutSpec;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import static com.facebook.litho.testing.assertj.ComponentViewAssert.times;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(ComponentsTestRunner.class)
public class MountStateTestItemTest {

  private static final String TEST_ID_1 = "test_id_1";
  private static final String TEST_ID_2 = "test_id_2";
  private static final String TEST_ID_3 = "test_id_3";
  private static final String MY_TEST_STRING_1 = "My test string";
  private static final String MY_TEST_STRING_2 = "My second test string";

  private ComponentContext mContext;

  @Before
  public void setup() {
    mContext = new ComponentContext(RuntimeEnvironment.application);
    ComponentsConfiguration.isEndToEndTestRun = true;
  }

  @After
  public void teardown() {
    ComponentsConfiguration.isEndToEndTestRun = false;
  }

  @Test
  public void testInnerComponentHostViewTags() {
    final LithoView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(
                    Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                        .child(TestDrawableComponent.create(c))
                        .child(TestDrawableComponent.create(c))
                        .testKey(TEST_ID_1))
                .child(TestDrawableComponent.create(c))
                .child(
                    TestDrawableComponent.create(c)
                        .withLayout().flexShrink(0)
                        .testKey(TEST_ID_2))
                .build();
          }
        });

    ComponentViewAssert.assertThat(componentView)
        .containsTestKey(TEST_ID_1)
        .containsTestKey(TEST_ID_2)
        .doesNotContainTestKey(TEST_ID_3);
  }

  @Test
  public void testMultipleIdenticalInnerComponentHostViewTags() {
    final LithoView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(
                    Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                        .child(TestDrawableComponent.create(c))
                        .child(TestDrawableComponent.create(c))
                        .testKey(TEST_ID_1))
                .child(TestDrawableComponent.create(c))
                .child(
                    TestDrawableComponent.create(c)
                        .withLayout().flexShrink(0)
                        .testKey(TEST_ID_1))
                .build();
          }
        });

    ComponentViewAssert.assertThat(componentView)
        .containsTestKey(TEST_ID_1, times(2))
        .doesNotContainTestKey(TEST_ID_2);
  }

  @Test
  public void testSkipInvalidTestKeys() {
    final LithoView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(
                    Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                        .child(TestDrawableComponent.create(c))
                        .child(TestDrawableComponent.create(c))
                        .testKey(""))
                .child(TestDrawableComponent.create(c))
                .child(
                    TestDrawableComponent.create(c)
                        .withLayout().flexShrink(0)
                        .testKey(null))
                .child(
                    TestDrawableComponent.create(c)
                        .withLayout().flexShrink(0)
                        .testKey(TEST_ID_1))
                .build();
          }
        });

    ComponentViewAssert.assertThat(componentView)
        .doesNotContainTestKey("")
        .doesNotContainTestKey(null)
        .containsTestKey(TEST_ID_1);
  }

  @Test
  public void testTextItemTextContent() {
    final LithoView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(
                    Text.create(c)
                        .text(MY_TEST_STRING_1)
                        .withLayout().flexShrink(0)
                        .testKey(TEST_ID_1))
                .build();
          }
        });

    ComponentViewAssert.assertThat(componentView).containsTestKey(TEST_ID_1);
    final TestItem item1 = ComponentViewTestHelper.findTestItem(componentView, TEST_ID_1);
    assertThat(item1.getTextContent()).isEqualTo(MY_TEST_STRING_1);
  }

  @Test
  public void testMultipleTextItemsTextContents() {
    final LithoView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(
                    Text.create(c)
                        .text(MY_TEST_STRING_1)
                        .withLayout().flexShrink(0)
                        .testKey(TEST_ID_1))
                .child(
                    Text.create(c)
                        .text(MY_TEST_STRING_2)
                        .withLayout().flexShrink(0)
                        .testKey(TEST_ID_2))
                .build();
          }
        });

    ComponentViewAssert.assertThat(componentView).containsTestKey(TEST_ID_1);
    final TestItem item1 = ComponentViewTestHelper.findTestItem(componentView, TEST_ID_1);
    assertThat(item1.getTextContent()).isEqualTo(MY_TEST_STRING_1);
    final TestItem item2 = ComponentViewTestHelper.findTestItem(componentView, TEST_ID_2);
    assertThat(item2.getTextContent()).isEqualTo(MY_TEST_STRING_2);
  }

  @Test
  public void testTextItemsWithClickHandler() {
    final LithoView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(
                    Text.create(c)
                        .text(MY_TEST_STRING_1)
                        .withLayout().flexShrink(0)
                        .clickHandler(mock(EventHandler.class))
                        .testKey(TEST_ID_1))
                .child(
                    Text.create(c)
                        .text(MY_TEST_STRING_2)
                        .withLayout().flexShrink(0)
                        .testKey(TEST_ID_2))
                .build();
          }
        });

    ComponentViewAssert.assertThat(componentView).containsTestKey(TEST_ID_1);
    final TestItem item1 = ComponentViewTestHelper.findTestItem(componentView, TEST_ID_1);
    assertThat(item1.getTextContent()).isEqualTo(MY_TEST_STRING_1);
    final TestItem item2 = ComponentViewTestHelper.findTestItem(componentView, TEST_ID_2);
    assertThat(item2.getTextContent()).isEqualTo(MY_TEST_STRING_2);
  }
}
