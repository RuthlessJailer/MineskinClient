package org.mineskin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mineskin.data.Skin;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class SyncMineskinClient {

	private static final String ID_FORMAT     = "https://api.mineskin.org/get/id/%s";
	private static final String URL_FORMAT    = "https://api.mineskin.org/generate/url?url=%s&%s";
	private static final String UPLOAD_FORMAT = "https://api.mineskin.org/generate/upload?%s";
	private static final String USER_FORMAT   = "https://api.mineskin.org/generate/user/%s?%s";

	private final String userAgent;

	private final JsonParser jsonParser = new JsonParser();
	private final Gson       gson       = new Gson();

	private long nextRequest = 0;

	public SyncMineskinClient() {
		this("MineSkin-JavaClient");
	}

	public SyncMineskinClient(final String userAgent) {
		this.userAgent = checkNotNull(userAgent);
	}

	public long getNextRequest() {
		return this.nextRequest;
	}

	/*
	 * ID
	 */

	/**
	 * Gets data for an existing Skin
	 *
	 * @param id Skin-Id
	 */
	@SneakyThrows
	public Skin getSkin(final int id) {
		final Connection connection = Jsoup
				.connect(String.format(ID_FORMAT, id))
				.userAgent(this.userAgent)
				.method(Connection.Method.GET)
				.ignoreContentType(true)
				.ignoreHttpErrors(true)
				.timeout(10000);

		final String body = connection.execute().body();

		return handleResponse(body);
	}

	/*
	 * URL
	 */

	Skin handleResponse(final String body) {
		final JsonObject jsonObject = this.jsonParser.parse(body).getAsJsonObject();

		if (jsonObject.has("error")) {
			throw new RuntimeException(jsonObject.get("error").getAsString());
		}

		final Skin skin = this.gson.fromJson(jsonObject, Skin.class);

		this.nextRequest = System.currentTimeMillis() + ((long) ((skin.nextRequest + 10) * 1000L));

		return skin;
	}

	/**
	 * Generates skin data from an URL (with default options)
	 *
	 * @param url URL
	 *
	 * @see #generateUrl(String, SkinOptions)
	 */
	public Skin generateUrl(final String url) {
		return generateUrl(url, SkinOptions.none());
	}

	/*
	 * Upload
	 */

	/**
	 * Generates skin data from an URL
	 *
	 * @param url     URL
	 * @param options {@link SkinOptions}
	 */
	@SneakyThrows
	public Skin generateUrl(final String url, final SkinOptions options) {
		checkNotNull(url);
		checkNotNull(options);

		if (System.currentTimeMillis() < this.nextRequest) {
			final long delay = (this.nextRequest - System.currentTimeMillis());
			Thread.sleep(delay + 1000);
		}

		final Connection connection = Jsoup
				.connect(String.format(URL_FORMAT, url, options.toUrlParam()))
				.userAgent(this.userAgent)
				.method(Connection.Method.POST)
				.ignoreContentType(true)
				.ignoreHttpErrors(true)
				.timeout(40000);

		final String body = connection.execute().body();

		return handleResponse(body);
	}

	/**
	 * Uploads and generates skin data from a local file (with default options)
	 *
	 * @param file File to upload
	 */
	public Skin generateUpload(final File file) {
		return generateUpload(file, SkinOptions.none());
	}

	/*
	 * User
	 */

	/**
	 * Uploads and generates skin data from a local file
	 *
	 * @param file    File to upload
	 * @param options {@link SkinOptions}
	 */
	@SneakyThrows
	public Skin generateUpload(final File file, final SkinOptions options) {
		checkNotNull(file);
		checkNotNull(options);

		if (System.currentTimeMillis() < this.nextRequest) {
			final long delay = (this.nextRequest - System.currentTimeMillis());
			Thread.sleep(delay + 1000);
		}

		final Connection connection = Jsoup
				.connect(String.format(UPLOAD_FORMAT, options.toUrlParam()))
				.userAgent(this.userAgent)
				.method(Connection.Method.POST)
				.data("file", file.getName(), new FileInputStream(file))
				.ignoreContentType(true)
				.ignoreHttpErrors(true)
				.timeout(40000);

		final String body = connection.execute().body();

		return handleResponse(body);
	}

	/**
	 * Loads skin data from an existing player (with default options)
	 *
	 * @param uuid {@link UUID} of the player
	 */
	public Skin generateUser(final UUID uuid) {
		return generateUser(uuid, SkinOptions.none());
	}

	/**
	 * Loads skin data from an existing player
	 *
	 * @param uuid    {@link UUID} of the player
	 * @param options {@link SkinOptions}
	 */
	@SneakyThrows
	public Skin generateUser(final UUID uuid, final SkinOptions options) {
		checkNotNull(uuid);
		checkNotNull(options);

		if (System.currentTimeMillis() < this.nextRequest) {
			final long delay = (this.nextRequest - System.currentTimeMillis());
			Thread.sleep(delay + 1000);
		}

		final Connection connection = Jsoup
				.connect(String.format(USER_FORMAT, uuid.toString(), options.toUrlParam()))
				.userAgent(this.userAgent)
				.method(Connection.Method.GET)
				.ignoreContentType(true)
				.ignoreHttpErrors(true)
				.timeout(40000);

		final String body = connection.execute().body();

		return handleResponse(body);
	}

}
