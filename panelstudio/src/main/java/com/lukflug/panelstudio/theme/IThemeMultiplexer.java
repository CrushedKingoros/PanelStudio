package com.lukflug.panelstudio.theme;

import java.awt.Color;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IInterface;

/**
 * Base class used to enable switch themes "on-the-fly".
 * It provides the renderers for the components.
 * In this way, the renderers can effectively be switched, without changing the field in the components itself.
 * @author lukflug
 */
@FunctionalInterface
public interface IThemeMultiplexer extends ITheme {
	@Override
	public default void loadAssets(IInterface inter) {
		getTheme().loadAssets(inter);
	}

	@Override
	public default IDescriptionRenderer getDescriptionRenderer() {
		IDescriptionRendererProxy proxy=()->getTheme().getDescriptionRenderer();
		return proxy;
	}

	@Override
	public default IContainerRenderer getContainerRenderer(int logicalLevel, int graphicalLevel) {
		IContainerRendererProxy proxy=()->getTheme().getContainerRenderer(logicalLevel,graphicalLevel);
		return proxy;
	}
	
	@Override
	public default <T> IPanelRenderer<T> getPanelRenderer (Class<T> type, int logicalLevel, int graphicalLevel) {
		IPanelRendererProxy<T> proxy=()->getTheme().getPanelRenderer(type,logicalLevel,graphicalLevel);
		return proxy;
	}
	
	@Override
	public default <T> IScrollBarRenderer<T> getScrollBarRenderer (Class<T> type, int logicalLevel, int graphicalLevel) {
		IScrollBarRendererProxy<T> proxy=()->getTheme().getScrollBarRenderer(type,logicalLevel,graphicalLevel);
		return proxy;
	}
	
	@Override
	public default <T> IEmptySpaceRenderer<T> getEmptySpaceRenderer (Class<T> type, int logicalLevel, int graphicalLevel) {
		IEmptySpaceRendererProxy<T> proxy=()->getTheme().getEmptySpaceRenderer(type,logicalLevel,graphicalLevel);
		return proxy;
	}

	@Override
	public default <T> IButtonRenderer<T> getButtonRenderer(Class<T> type, int logicalLevel, int graphicalLevel, boolean container) {
		IButtonRendererProxy<T> proxy=()->getTheme().getButtonRenderer(type,logicalLevel,graphicalLevel,container);
		return proxy;
	}

	@Override
	public default IButtonRenderer<IBoolean> getCheckMarkRenderer(int logicalLevel, int graphicalLevel, boolean container) {
		IButtonRendererProxy<IBoolean> proxy=()->getTheme().getCheckMarkRenderer(logicalLevel,graphicalLevel,container);
		return proxy;
	}

	@Override
	public default IButtonRenderer<String> getKeybindRenderer(int logicalLevel, int graphicalLevel, boolean container) {
		IButtonRendererProxy<String> proxy=()->getTheme().getKeybindRenderer(logicalLevel,graphicalLevel,container);
		return proxy;
	}

	@Override
	public default ISliderRenderer getSliderRenderer(int logicalLevel, int graphicalLevel, boolean container) {
		ISliderRendererProxy proxy=()->getTheme().getSliderRenderer(logicalLevel,graphicalLevel,container);
		return proxy;
	}

	@Override
	public default int getBaseHeight() {
		return getTheme().getBaseHeight();
	}

	@Override
	public default Color getMainColor(boolean focus, boolean active) {
		return getTheme().getMainColor(focus,active);
	}

	@Override
	public default Color getBackgroundColor(boolean focus) {
		return getTheme().getBackgroundColor(focus);
	}

	@Override
	public default Color getFontColor(boolean focus) {
		return getTheme().getFontColor(focus);
	}

	@Override
	public default void overrideMainColor(Color color) {
		getTheme().overrideMainColor(color);
	}

	@Override
	public default void restoreMainColor() {
		getTheme().restoreMainColor();
	}
	
	/**
	 * Abstract method that returns the current theme.
	 * @return the current theme
	 */
	public ITheme getTheme();
}
