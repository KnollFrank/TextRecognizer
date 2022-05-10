package com.google.mlkit.vision.demo.java.textdetector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.junit.Test;
import org.textrecognizer.TestUtils;
import org.textrecognizer.common.AssetsUtils;

import java.io.File;
import java.util.concurrent.CountDownLatch;

public class TextRecognitionProcessorTest {

    @Test
    public void shouldDetectTextInImage() throws InterruptedException {
        // Given
        final Context context = TestUtils.getContext();
        final CountDownLatch signal = new CountDownLatch(1);
        final TextRecognitionProcessor imageProcessor =
                new TextRecognitionProcessor(
                        context,
                        new TextRecognizerOptions.Builder().build()) {

                    @Override
                    protected void onSuccess(@NonNull final Text text) {
                        super.onSuccess(text);
                        // Then
                        assertThat(text.getText(), is("9\n5\n8\nF"));
                        signal.countDown();
                    }

                    @Override
                    protected void onFailure(@NonNull final Exception e) {
                        super.onFailure(e);
                        // Then
                        fail();
                    }
                };
        final Bitmap captcha =
                BitmapFactory.decodeStream(
                        AssetsUtils.open(
                                context.getAssets(),
                                new File("captchas", "captcha_image_4.jpeg")));

        // When
        imageProcessor.processBitmap(captcha);
        signal.await();
    }
}
