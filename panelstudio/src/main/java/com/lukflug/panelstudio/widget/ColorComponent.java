package com.lukflug.panelstudio.widget;

import java.awt.Color;

import com.lukflug.panelstudio.base.Animation;
import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.base.SimpleToggleable;
import com.lukflug.panelstudio.setting.IColorSetting;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import com.lukflug.panelstudio.theme.IColorScheme;
import com.lukflug.panelstudio.theme.IContainerRenderer;
import com.lukflug.panelstudio.theme.IPanelRenderer;
import com.lukflug.panelstudio.theme.IRenderer;

/**
 * Component representing a color-valued setting.
 * @author lukflug
 */
public class ColorComponent extends CollapsibleContainer {
	/**
	 * The setting in question.
	 */
	protected IColorSetting setting;
	/**
	 * Whether to render an alpha slider.
	 */
	protected final boolean alpha;
	/**
	 * Whether to render a rainbow button.
	 */
	protected final boolean rainbow;
	/**
	 * Custom {@link IColorScheme} that set the active color to the value of the setting.
	 */
	protected IColorScheme scheme,overrideScheme;
	/**
	 * {@link IToggleable} indicating whether to use RGB (false) or HSB (true).
	 */
	protected IToggleable colorModel;
	
	/**
	 * Constructor.
	 * @param title the name of the setting
	 * @param description the description for this component
	 * @param renderer the renderer for the color setting container
	 * @param animation the animation for opening and closing
	 * @param componentRenderer the renderer for the children of the container
	 * @param setting the setting in question
	 * @param alpha whether to render an alpha slider
	 * @param rainbow whether to render a rainbow slider
	 * @param colorModel {@link IToggleable} indicating whether to use RGB (false) or HSB (true)
	 */
	public ColorComponent(IColorSetting setting, Animation animation, IPanelRenderer panelRenderer, IButtonRenderer<Void> titleRenderer, IButtonRenderer<IBoolean> buttonRenderer, IContainerRenderer sliderRenderer) {
		super(setting.getDisplayName(),setting.getDescription(),setting.isVisible(),()->true,new SimpleToggleable(false),animation,panelRenderer,titleRenderer,sliderRenderer,null,null);
		this.setting=setting;
		//this.alpha=alpha;
		//this.rainbow=rainbow;
		scheme=new ColorSettingScheme(null);
		overrideScheme=new ColorSettingScheme(null);
		//this.colorModel=colorModel;
		addComponent(new ToggleButton("Rainbow",null,()->setting.allowsRainbow(),new IToggleable() {
			@Override
			public boolean isOn() {
				return setting.getRainbow();
			}

			@Override
			public void toggle() {
				setting.setRainbow(!setting.getRainbow());
			}
		},buttonRenderer));
		addComponent(new ColorSlider(componentRenderer,0));
		addComponent(new ColorSlider(componentRenderer,1));
		addComponent(new ColorSlider(componentRenderer,2));
		if (alpha) addComponent(new ColorSlider(componentRenderer,3));
	}
	
	/**
	 * Override the {@link IColorScheme} and render the container.
	 */
	@Override
	public void render (Context context) {
		renderer.overrideColorScheme(scheme);
		super.render(context);
		renderer.restoreColorScheme();
	}
	
	
	/**
	 * Class to render the sliders in the color container.
	 * @author lukflug
	 */
	protected class ColorSlider extends Slider {
		/**
		 * Number indicating the index of the component for the color model.
		 */
		private final int value;
		
		/**
		 * Constructor.
		 * @param renderer the {@link IRenderer} for the component
		 * @param value index of slider
		 */
		public ColorSlider(IRenderer renderer, int value) {
			super("",null,renderer);
			this.value=value;
		}
		
		/**
		 * Override the {@link ColorScheme} and render the component with the caption containing the name of the component in the color model and the value for that component.
		 */
		@Override
		public void render (Context context) {
			title=getTitle(value)+(int)(getMax()*getValue());
			//renderer.overrideColorScheme(overrideScheme);
			super.render(context);
			//renderer.restoreColorScheme();
		}

		/**
		 * Implementation for {@link Slider#getValue()}.
		 */
		@Override
		protected double getValue() {
			Color c=setting.getColor();
			if (value<3) {
				if (colorModel.isOn()) return Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),null)[value];
				switch (value) {
				case 0:
					return c.getRed()/255.0;
				case 1:
					return c.getGreen()/255.0;
				case 2:
					return c.getBlue()/255.0;
				}
			}
			return c.getAlpha()/255.0;
		}

		/**
		 * Implementation for {@link Slider#setValue(double)}.
		 */
		@Override
		protected void setValue(double value) {
			Color c=setting.getColor();
			float[] color=Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),null);
			switch (this.value) {
			case 0:
				if (colorModel.isOn()) c=Color.getHSBColor((float)value,color[1],color[2]);
				else c=new Color((int)(255*value),c.getGreen(),c.getBlue());
				if (alpha) setting.setValue(new Color(c.getRed(),c.getGreen(),c.getBlue(),setting.getColor().getAlpha()));
				else setting.setValue(c);
				break;
			case 1:
				if (colorModel.isOn()) c=Color.getHSBColor(color[0],(float)value,color[2]);
				else c=new Color(c.getRed(),(int)(255*value),c.getBlue());
				if (alpha) setting.setValue(new Color(c.getRed(),c.getGreen(),c.getBlue(),setting.getColor().getAlpha()));
				else setting.setValue(c);
				break;
			case 2:
				if (colorModel.isOn()) c=Color.getHSBColor(color[0],color[1],(float)value);
				else c=new Color(c.getRed(),c.getGreen(),(int)(255*value));
				if (alpha) setting.setValue(new Color(c.getRed(),c.getGreen(),c.getBlue(),setting.getColor().getAlpha()));
				else setting.setValue(c);
				break;
			case 3:
				setting.setValue(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(255*value)));
				break;
			}
		}
		
		/**
		 * Get the caption of the component based on index and colorModel.
		 * @param value the index of the slider
		 * @return caption for the slider
		 */
		protected String getTitle (int value) {
			switch (value) {
			case 0:
				return (colorModel.isOn()?"Hue:":"Red:")+" \u00A77";
			case 1:
				return (colorModel.isOn()?"Saturation:":"Green:")+" \u00A77";
			case 2:
				return (colorModel.isOn()?"Brightness:":"Blue:")+" \u00A77";
			case 3:
				return "Alpha: \u00A77";
			}
			return "";
		}
		
		protected int getMax() {
			if (!colorModel.isOn()) return 255;
			else if (value==0) return 360;
			else if (value<3) return 100;
			else return 255;
		}
	}
	
	
	/**
	 * {@link IColorScheme} to override the active color to the current value of the color setting.
	 * @author lukflug
	 */
	protected class ColorSettingScheme implements IColorScheme {
		/**
		 * {@link IColorScheme} to be overridden.
		 */
		IColorScheme scheme;
		
		/**
		 * Constructor.
		 * @param renderer the {@link IRenderer} to override
		 */
		public ColorSettingScheme (IRenderer renderer) {
			scheme=renderer.getDefaultColorScheme();
		}
		
		/**
		 * Return the current color setting, instead of the active color defined by the scheme.
		 */
		@Override
		public Color getActiveColor() {
			return setting.getValue();
		}

		/**
		 * Return the color defined by the scheme.
		 */
		@Override
		public Color getInactiveColor() {
			return scheme.getInactiveColor();
		}

		/**
		 * Return the color defined by the scheme.
		 */
		@Override
		public Color getBackgroundColor() {
			return scheme.getBackgroundColor();
		}

		/**
		 * Return the color defined by the scheme.
		 */
		@Override
		public Color getOutlineColor() {
			return scheme.getOutlineColor();
		}

		/**
		 * Return the color defined by the scheme.
		 */
		@Override
		public Color getFontColor() {
			return scheme.getFontColor();
		}

		/**
		 * Return the value defined by the scheme.
		 */
		@Override
		public int getOpacity() {
			return scheme.getOpacity();
		}
	}
}
