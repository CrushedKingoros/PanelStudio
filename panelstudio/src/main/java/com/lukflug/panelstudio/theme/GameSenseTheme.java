package com.lukflug.panelstudio.theme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IInterface;

/**
 * Recreates the appearance of GameSense 2.2.0.
 * @author lukflug
 */
public class GameSenseTheme extends ThemeBase {
	protected final int height,border,scroll;
	protected final String separator;
	
	public GameSenseTheme (IColorScheme scheme, int height, int border, int scroll, String separator) {
		super(scheme);
		this.height=height;
		this.border=border;
		this.scroll=scroll;
		this.separator=separator;
		scheme.createSetting(this,"Title Color","The color for panel titles.",false,true,new Color(255,0,0),false);
		scheme.createSetting(this,"Outline Color","The color for panel outlines.",false,true,new Color(255,0,0),false);
		scheme.createSetting(this,"Enabled Color","The main color for enabled components.",true,true,new Color(255,0,0,150),false);
		scheme.createSetting(this,"Disabled Color","The main color for disabled modules.",false,true,new Color(0,0,0),false);
		scheme.createSetting(this,"Settings Color","The background color for settings.",false,true,new Color(32,32,32),false);
		scheme.createSetting(this,"Font Color","The main color for text.",false,true,new Color(255,255,255),false);
	}
	
	protected void fillBaseRect (Context context, boolean focus, boolean active, int level) {
		Color color=getMainColor(focus,active);
		if (level>1 && !active) color=getBackgroundColor(focus);
		else if (level==0 && active) color=ITheme.combineColors(getColor(scheme.getColor("Title Color")),scheme.getColor("Enabled Color"));
		context.getInterface().fillRect(context.getRect(),color,color,color,color);
	}
	
	protected void renderOverlay (Context context) {
		Color color=context.isHovered()?new Color(255,255,255,64):new Color(0,0,0,0);
		context.getInterface().fillRect(context.getRect(),color,color,color,color);
	}
	
	@Override
	public IDescriptionRenderer getDescriptionRenderer() {
		return new IDescriptionRenderer() {
			@Override
			public void renderDescription(IInterface inter, Point pos, String text) {
				Rectangle rect=new Rectangle(pos,new Dimension(inter.getFontWidth(text),inter.getFontHeight()));
				Color color=getMainColor(true,false);
				inter.fillRect(rect,color,color,color,color);
				inter.drawString(pos,text,getFontColor(true));
				Color bordercolor=scheme.getColor("Outline Color");
				inter.fillRect(new Rectangle(rect.x,rect.y,rect.width,1),bordercolor,bordercolor,bordercolor,bordercolor);
				inter.fillRect(new Rectangle(rect.x,rect.y+rect.height-1,rect.width,1),bordercolor,bordercolor,bordercolor,bordercolor);
				inter.fillRect(new Rectangle(rect.x,rect.y,1,rect.height),bordercolor,bordercolor,bordercolor,bordercolor);
				inter.fillRect(new Rectangle(rect.x+rect.width-1,rect.y,1,rect.height),bordercolor,bordercolor,bordercolor,bordercolor);
			}
		};
	}

	@Override
	public IContainerRenderer getContainerRenderer(int level) {
		return new IContainerRenderer() {
			@Override
			public void renderBackground (Context context, boolean focus) {
				if (level!=0) {
					Color color=scheme.getColor("Outline Color");
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y,context.getSize().width,1),color,color,color,color);
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y+context.getSize().height-1,context.getSize().width,1),color,color,color,color);
				}
			}
			
			@Override
			public int getTop() {
				return level==0?0:1;
			}
			
			@Override
			public int getBottom() {
				return level==0?0:1;
			}
		};
	}
	
	@Override
	public <T> IPanelRenderer<T> getPanelRenderer (Class<T> type, int level) {
		return new IPanelRenderer<T>() {
			@Override
			public int getLeft() {
				return level==0?1:0;
			}
			
			@Override
			public int getRight() {
				return level==0?1:0;
			}
			
			@Override
			public void renderPanelOverlay(Context context, boolean focus, T state) {
				if (level==0) {
					Color color=scheme.getColor("Outline Color");
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y,context.getSize().width,1),color,color,color,color);
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y+context.getSize().height-1,context.getSize().width,1),color,color,color,color);
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y,1,context.getSize().height),color,color,color,color);
					context.getInterface().fillRect(new Rectangle(context.getPos().x+context.getSize().width-1,context.getPos().y,1,context.getSize().height),color,color,color,color);
				}
			}
		};
	}
	
	@Override
	public <T> IScrollBarRenderer<T> getScrollBarRenderer (Class<T> type, int level) {
		return new IScrollBarRenderer<T>() {
			@Override
			public int renderScrollBar(Context context, boolean focus, T state, boolean horizontal, int height, int position) {
				Color activecolor=getMainColor(focus,true);
				Color inactivecolor=getMainColor(focus,false);
				if (horizontal) {
					int a=(int)(position/(double)height*context.getSize().width);
					int b=(int)((position+context.getSize().width)/(double)height*context.getSize().width);
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y,a,context.getSize().height),inactivecolor,inactivecolor,inactivecolor,inactivecolor);
					context.getInterface().fillRect(new Rectangle(context.getPos().x+a,context.getPos().y,b-a,context.getSize().height),activecolor,activecolor,activecolor,activecolor);
					context.getInterface().fillRect(new Rectangle(context.getPos().x+b,context.getPos().y,context.getSize().width-b,context.getSize().height),inactivecolor,inactivecolor,inactivecolor,inactivecolor);
				} else {
					int a=(int)(position/(double)height*context.getSize().height);
					int b=(int)((position+context.getSize().height)/(double)height*context.getSize().height);
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y,context.getSize().width,a),inactivecolor,inactivecolor,inactivecolor,inactivecolor);
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y+a,context.getSize().width,b-a),activecolor,activecolor,activecolor,activecolor);
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y+b,context.getSize().width,context.getSize().height-b),inactivecolor,inactivecolor,inactivecolor,inactivecolor);
				}
				Color bordercolor=scheme.getColor("Outline Color");
				if (horizontal) context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y,context.getSize().width,1),bordercolor,bordercolor,bordercolor,bordercolor);
				else context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y,1,context.getSize().height),bordercolor,bordercolor,bordercolor,bordercolor);
				if (horizontal) return (int)((context.getInterface().getMouse().x-context.getPos().x)*height/(double)context.getSize().width-context.getSize().width/2.0);
				else return (int)((context.getInterface().getMouse().y-context.getPos().y)*height/(double)context.getSize().height-context.getSize().height/2.0);
			}

			@Override
			public int getThickness() {
				return scroll;
			}
		};
	}
	
	@Override
	public <T> IEmptySpaceRenderer<T> getEmptySpaceRenderer (Class<T> type, int level) {
		return (context,focus,state)->{
			Color color=scheme.getColor("Outline Color");
			context.getInterface().fillRect(context.getRect(),color,color,color,color);
		};
	}
	
	@Override
	public <T> IButtonRenderer<T> getButtonRenderer (Class<T> type, int level, boolean container) {
		return new IButtonRenderer<T>() {
			@Override
			public void renderButton(Context context, String title, boolean focus, T state) {
				if (type==IBoolean.class) fillBaseRect(context,focus,((IBoolean)state).isOn(),level);
				else fillBaseRect(context,focus,level==0,level);
				if (level==0 && container) {
					Color color=scheme.getColor("Outline Color");
					context.getInterface().fillRect(new Rectangle(context.getPos().x,context.getPos().y+context.getSize().height-1,context.getSize().width,1),color,color,color,color);
				}
				renderOverlay(context);
				if (type==String.class) context.getInterface().drawString(context.getRect().getLocation(),title+separator+state,getFontColor(focus));
				else context.getInterface().drawString(context.getRect().getLocation(),title,getFontColor(focus));
			}

			@Override
			public int getDefaultHeight() {
				return getBaseHeight();
			}
		};
	}

	@Override
	public IButtonRenderer<IBoolean> getCheckMarkRenderer(int level, boolean container) {
		return getButtonRenderer(IBoolean.class,level,container);
	}

	@Override
	public IButtonRenderer<String> getKeybindRenderer(int level, boolean container) {
		return new IButtonRenderer<String>() {
			@Override
			public void renderButton(Context context, String title, boolean focus, String state) {
				fillBaseRect(context,focus,focus,level);
				renderOverlay(context);
				context.getInterface().drawString(context.getRect().getLocation(),title+separator+(focus?"...":state),getFontColor(focus));
			}

			@Override
			public int getDefaultHeight() {
				return getBaseHeight();
			}
		};
	}

	@Override
	public ISliderRenderer getSliderRenderer(int level, boolean container) {
		return new ISliderRenderer() {
			@Override
			public void renderSlider(Context context, String title, String state, boolean focus, double value) {
				Color colorA=getMainColor(focus,true),colorB=getBackgroundColor(focus);
				Rectangle rect=getSlideArea(context);
				int divider=(int)(rect.width*value);
				context.getInterface().fillRect(new Rectangle(rect.x,rect.y,divider,rect.height),colorA,colorA,colorA,colorA);
				context.getInterface().fillRect(new Rectangle(rect.x+divider,rect.y,rect.width-divider,rect.height),colorB,colorB,colorB,colorB);
				renderOverlay(context);
				context.getInterface().drawString(context.getRect().getLocation(),title+separator+state,getFontColor(focus));
			}

			@Override
			public int getDefaultHeight() {
				return getBaseHeight();
			}
		};
	}

	@Override
	public int getBaseHeight() {
		return height+2*border;
	}

	@Override
	public Color getMainColor(boolean focus, boolean active) {
		if (active) return ITheme.combineColors(getColor(scheme.getColor("Enabled Color")),scheme.getColor("Enabled Color"));
		else return ITheme.combineColors(scheme.getColor("Disabled Color"),scheme.getColor("Enabled Color"));
	}

	@Override
	public Color getBackgroundColor(boolean focus) {
		return ITheme.combineColors(scheme.getColor("Settings Color"),scheme.getColor("Enabled Color"));
	}

	@Override
	public Color getFontColor(boolean focus) {
		return scheme.getColor("Font Color");
	}
}
