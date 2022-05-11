package org.textrecognizer.textdetector;

import static org.junit.Assert.fail;
import static org.textrecognizer.common.IOUtils.getFileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.junit.Test;
import org.textrecognizer.TestUtils;
import org.textrecognizer.common.AssetsUtils;
import org.textrecognizer.common.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
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
                        Log.i(this.getClass().getSimpleName(), "FKK-TEXT: " + text.getText());
                        final FileOutputStream fileOutputStream = getFileOutputStream(new File(context.getFilesDir(), "captcha_image.txt"));
                        IOUtils.persist(text.getText(), fileOutputStream);
                        // assertThat(text.getText(), is("9\n5\n8\nF"));
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
                                new File("captchas", "captcha_image.jpeg")));

        // When
        imageProcessor.processBitmap(captcha);
        signal.await();
    }
}
