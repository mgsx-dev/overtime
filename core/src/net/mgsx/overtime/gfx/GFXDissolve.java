package net.mgsx.overtime.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.mgsx.overtime.OverTime;
import net.mgsx.overtime.utils.PixelPerfectViewport;

public class GFXDissolve extends ScreenAdapter
{

	private float time;
	private SpriteBatch batch;
	private Viewport viewport;
	private Texture noiseTexture;
	private ShaderProgram shader;
	
	public GFXDissolve() {
		noiseTexture = new Texture(Gdx.files.internal("noise.png"));
		viewport = new PixelPerfectViewport(OverTime.WORLD_WIDTH, OverTime.WORLD_HEIGHT);
		shader = new ShaderProgram(Gdx.files.internal("shaders/dissolve.vs"), Gdx.files.internal("shaders/dissolve.fs"));
		batch = new SpriteBatch(4, shader);
	}
	
	
	public void setTime(float time) 
	{
		this.time = time;
	}

	@Override
	public void render(float delta) 
	{
		viewport.apply();
		batch.enableBlending();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
		shader.setUniformf("u_colorOn", 0, 0, 0, 1);
		shader.setUniformf("u_colorOff", 0, 0, 0, 0);
		shader.setUniformf("u_dissolve", time);
		//batch.setColor(1, 1, 1, time);
		batch.draw(noiseTexture, 0, 0);
		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

}
