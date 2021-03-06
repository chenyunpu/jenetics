/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jenetics.example.image;

import static java.lang.Math.min;
import static java.util.Objects.requireNonNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Draws the given {@code PolygonChromosome}.
 */
final class PolygonPanel extends JPanel {
	private int _width;
	private int _height;
	private PolygonChromosome _chromosome;

	PolygonPanel(int width, int height) {
		_width = width;
		_height = height;
	}

	PolygonPanel() {
		this(10, 10);
	}

	public void setDimension(final int width, final int height) {
		_width = width;
		_height = height;
	}

	public Dimension getDimension() {
		return new Dimension(_width, _height);
	}

	public void setChromosome(final PolygonChromosome chromosome) {
		_chromosome = requireNonNull(chromosome);
	}

	public PolygonChromosome getChromosome() {
		return _chromosome;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		if (_chromosome != null) {
			_chromosome.draw((Graphics2D) g, width(), height());
		} else {
			g.setColor(Color.white);
			g.clearRect(0, 0, width(), height());
		}
	}

	private float scaleFactor() {
		final float sw = getWidth()/(float)_width;
		final float sh = getHeight()/(float)_height;
		return min(sw, sh);
	}

	private int width() {
		return (int) (_width*scaleFactor());
	}

	private int height() {
		return (int) (_height*scaleFactor());
	}

}
