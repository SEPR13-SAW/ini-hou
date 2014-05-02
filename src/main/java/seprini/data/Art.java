package seprini.data;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

/**
 * Loads all the requires art (textures), essentially pre-loading all the
 * required texture for later use
 * 
 * @author Crembo, Miguel
 * 
 */
public class Art {

	/**
	 * A hashtable which stores all of the textures
	 */
	private final static Hashtable<String, TextureRegion> textures = new Hashtable<String, TextureRegion>();

	/**
	 * A hashtable which stores all of the sounds
	 */
	private final static Hashtable<String, Playable> sounds = new Hashtable<String, Playable>();

	/**
	 * A skin can be loaded via JSON or defined programmatically, either is
	 * fine. Using a skin is optional but strongly recommended solely for the
	 * convenience of getting a texture, region, etc as a drawable, tinted
	 * drawable, etc.
	 */
	private final static Skin skin = new Skin();

	/** If true, art is replaced by fake objects used for testing */
	private static boolean useFakeArt = false;

	/**
	 * Enables / disables fake art objects
	 *
	 * @param value if true, getters return fake artwork objects
	 */
	static void setUseFakeArt(boolean value) { useFakeArt = value; }

	/**
	 * Initialises loading of texture, should be called once
	 * 
	 */
	public static void load() {
		// loads the whole sprite which consists most of the game's textures
		Texture combined = loadTexture("data/combinedgraphics.png");
		combined.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);

		// splits up the sprite into parts and loads them into the table
		textures.put("airspace", split(combined, 0, 0, 1079, 720));
		textures.put("sidebar", split(combined, 1080, 0, 200,720));
		textures.put("aircraft", split(combined, 1281, 0, 72, 63));
		textures.put("waypoint", split(combined, 0, 720, 20, 20));
		textures.put("menuAircraft", split(combined, 1280, 64, 727, 249));
		textures.put("libgdx", split(combined, 1353, 0, 53, 10));
		textures.put("entrypoint", split(combined, 20, 720, 20, 20));
		textures.put("exitpoint", split(combined, 40, 720, 20, 20));
		textures.put("airport", split(combined, 0, 741, 154, 120));
		textures.put("dot", split(combined, 1285, 240, 1, 1));

		// load the default skin
		loadSkin();

		// load the sound effects
		loadSounds();
	}

	private static void loadSounds() {
		sounds.put("ding", loadSound("ding.wav", false));
		sounds.put("warning", loadSound("warning.mp3", false));
		sounds.put("crash", loadSound("crash.mp3", false));
		sounds.put("comeflywithme", loadSound("comeflywithme.mp3", true));
		sounds.put("ambience", loadSound("ambience.mp3", true));
	}

	/**
	 * Loads the default skin
	 */
	private static void loadSkin() {
		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		// Add the font to the skin; TODO: use a different one?
		BitmapFont font = new BitmapFont();
		BitmapFont textFieldFont = new BitmapFont();
		textFieldFont.setScale(1.0f);

		skin.add("default", font);
		skin.add("text-field", textFieldFont);

		// Configure a TextButtonStyle and name it "default". Skin resources are
		// stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", 0.07f, 0.1f, 0.22f, 1);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		// Toggle style is the same as default, except it has a checked colour as well
		TextButtonStyle toggleStyle = new TextButtonStyle(textButtonStyle);
		toggleStyle.checked = skin.newDrawable("white", Color.BLUE);
		skin.add("toggle", toggleStyle);

		// labelStyle with background
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		labelStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
		skin.add("default", labelStyle);

		// labelStyle without a background and black text
		LabelStyle textStyle = new LabelStyle();
		textStyle.font = skin.getFont("default");
		textStyle.fontColor = Color.BLACK;
		skin.add("textStyle", textStyle);
		
		TextFieldStyle textFieldStyle = new TextFieldStyle();
		textFieldStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
		textFieldStyle.font = skin.getFont("text-field");
		textFieldStyle.fontColor = Color.WHITE;
		skin.add("default", textFieldStyle);
		
		ListStyle listStyle = new ListStyle();
		listStyle.font = skin.getFont("text-field");
		listStyle.fontColorUnselected = Color.BLACK;
		listStyle.fontColorSelected = Color.WHITE;
		listStyle.selection = skin.newDrawable("white", 0.07f, 0.1f, 0.22f, 1);
		skin.add("default", listStyle);
		
		ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
		scrollPaneStyle.vScrollKnob = skin.newDrawable("white", Color.DARK_GRAY);
		scrollPaneStyle.vScrollKnob.setMinWidth(3f);
		scrollPaneStyle.vScroll = skin.newDrawable("white", Color.DARK_GRAY);
		scrollPaneStyle.vScroll.setMinWidth(1f);
		skin.add("default", scrollPaneStyle);

	}

	/**
	 * Splits a texture from given position with size and width
	 *
	 * @return The requested TextureRegion
	 */
	private static TextureRegion split(Texture texture, int x, int y, int width, int height) {
		return new TextureRegion(texture, x, y, width, height);
	}

	/**
	 * Wrapper for loading a texture
	 * 
	 * @param textureName
	 * @return Texture
	 */
	private static Texture loadTexture(String textureName) {
		return new Texture(Gdx.files.internal(textureName), true);
	}

	/**
	 * Loads a sound effects file
	 *
	 * @param soundName filename of the sound file
	 * @return the new sound
	 */
	private static Playable loadSound(String soundName, boolean music) {
		FileHandle file = Gdx.files.internal("sounds/" + soundName);

		if (music)
			return new MusicImpl(Gdx.audio.newMusic(file));
		else
			return new SoundImpl(Gdx.audio.newSound(file));
	}

	/**
	 * Returns a texture region, should be used for all drawing all models
	 * 
	 * @param key
	 * @return the required texture region or null if doesn't exist
	 */
	public static TextureRegion getTextureRegion(String key) {
		if (useFakeArt)
			return new TextureRegion();

		if (!textures.containsKey(key))
			return null;

		return textures.get(key);
	}

	/**
	 * Get a sound from the sound pool
	 * 
	 * @param key
	 * @return the required sound or null if key doesn't exist
	 */
	public static Playable getSound(String key) {
		if (useFakeArt)
			return new FakeSoundImpl();

		if (!sounds.containsKey(key))
			return null;

		return sounds.get(key);
	}

	public static Skin getSkin() {
		return skin;
	}

	private static class SoundImpl implements Playable
	{
		private final Sound sound;

		private SoundImpl(Sound sound)
		{
			this.sound = sound;
		}

		@Override
		public void play() { sound.play(); }

		@Override
		public void playLooping() { sound.setLooping(sound.play(), true); }

		@Override
		public void play(float volume) { sound.play(volume); }

		@Override
		public void playLooping(float volume) { sound.setLooping(sound.play(volume), true); }

		@Override
		public void stop() { sound.stop(); }

		@Override
		public void dispose() { sound.dispose();}
	}

	private static class MusicImpl implements Playable
	{
		private final Music music;

		private MusicImpl(Music music)
		{
			this.music = music;
		}

		@Override
		public void play() { play(1); }

		@Override
		public void playLooping() { playLooping(1); }

		@Override
		public void play(float volume)
		{
			music.setLooping(false);
			music.setVolume(volume);
			music.play();
		}

		@Override
		public void playLooping(float volume)
		{
			music.setLooping(true);
			music.setVolume(volume);
			music.play();
		}

		@Override
		public void stop() { music.stop(); }

		@Override
		public void dispose() { music.dispose();}
	}

	private static class FakeSoundImpl implements Playable
	{
		@Override public void play() {}
		@Override public void playLooping() {}
		@Override public void play(float volume) {}
		@Override public void playLooping(float volume) {}
		@Override public void stop() {}
		@Override public void dispose() {}
	}
}
