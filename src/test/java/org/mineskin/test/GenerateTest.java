package org.mineskin.test;

import org.junit.Test;
import org.mineskin.SkinOptions;
import org.mineskin.SyncMineskinClient;
import org.mineskin.data.Skin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenerateTest {

	private final SyncMineskinClient client = new SyncMineskinClient();

	@Test(timeout = 90000L)
	public void urlTest() throws InterruptedException {
		Thread.sleep(7000);

		final String name = "TestJavaUrl";
		final Skin   skin = this.client.generateUrl("https://i.imgur.com/0Fna2GH.png", SkinOptions.name(name));
		validateSkin(skin, name);

		Thread.sleep(1000);
	}

	void validateSkin(final Skin skin, final String name) {
		assertNotNull(skin);
		assertNotNull(skin.data);
		assertNotNull(skin.data.texture);

		assertEquals(name, skin.name);
	}

	@Test(timeout = 90000L)
	public void uploadTest() throws InterruptedException, IOException {
		Thread.sleep(7000);

		final String name = "TestJavaUpload";
		final File   file = File.createTempFile("mineskin-temp-upload-image", ".png");
		ImageIO.write(randomImage(64, 32), "png", file);
		final Skin skin = this.client.generateUpload(file, SkinOptions.name(name));
		validateSkin(skin, name);

		Thread.sleep(1000);
	}

	BufferedImage randomImage(final int width, final int height) {
		final Random        random = new Random();
		final BufferedImage image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final float r = random.nextFloat();
				final float g = random.nextFloat();
				final float b = random.nextFloat();
				image.setRGB(x, y, new Color(r, g, b).getRGB());
			}
		}
		return image;
	}

}
