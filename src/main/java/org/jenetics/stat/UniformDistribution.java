/*
 * Java Genetic Algorithm Library (@!identifier!@).
 * Copyright (c) @!year!@ Franz Wilhelmstötter
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 *     Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 *     
 */
package org.jenetics.stat;

import static org.jenetics.util.object.eq;
import static org.jenetics.util.object.hashCodeOf;
import static org.jenetics.util.object.nonNull;

import java.util.List;

import javolution.text.Text;
import javolution.util.FastList;

import org.jscience.mathematics.function.Function;
import org.jscience.mathematics.function.Variable;
import org.jscience.mathematics.number.Float64;

import org.jenetics.util.Range;


/**
 * <a href="http://en.wikipedia.org/wiki/Uniform_distribution_%28continuous%29">
 * Uniform distribution</a> class.
 * 
 * @see LinearDistribution
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class UniformDistribution<
	N extends Number & Comparable<? super N>
>
	implements Distribution<N> 
{

	/**
	 * <p>
	 * <img 
	 *     src="doc-files/uniform-pdf.gif"
	 *     alt="f(x)=\left\{\begin{matrix}
	 *          \frac{1}{max-min} & for & x \in [min, max] \\ 
	 *          0 & & otherwise \\
	 *          \end{matrix}\right."
	 * />
	 * </p>
	 * 
	 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
	 * @version $Id$
	 */
	static final class PDF<N extends Number & Comparable<? super N>> 
		extends Function<N, Float64> 
	{
		private static final long serialVersionUID = 1L;
		
		// Create and initialize the used variable 'x'.
		private final Variable<N> _variable = new Variable.Local<>("x");
		private final List<Variable<N>> _variables = new FastList<>(1);
		{ _variables.add(_variable); }
		
		private final double _min;
		private final double _max;
		private final Float64 _probability;
		
		public PDF(final Range<N> domain) {
			_min = domain.getMin().doubleValue();
			_max = domain.getMax().doubleValue();
			_probability = Float64.valueOf(1.0/(_max - _min));
		}
		
		@Override
		public Float64 evaluate() {
			final double x = _variable.get().doubleValue();
			
			Float64 result = Float64.ZERO;
			if (x >= _min && x <= _max) {
				result = _probability;
			}
			
			return result;
		}
	
		@Override
		public List<Variable<N>> getVariables() {
			return _variables;
		}
	
		@Override
		public Text toText() {
			return Text.valueOf(String.format("p(x) = %s", _probability));
		}
		
	}	
	
	/**
	 * <p>
	 * <img 
	 *     src="doc-files/uniform-cdf.gif"
	 *     alt="f(x)=\left\{\begin{matrix}
	 *         0 & for & x < min \\ 
	 *         \frac{x-min}{max-min} & for & x \in [min, max] \\
	 *         1 & for & x > max  \\ 
	 *         \end{matrix}\right."
	 * />
	 * </p>
	 * 
	 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
	 * @version $Id$
	 */
	static final class CDF<N extends Number & Comparable<? super N>> 
		extends Function<N, Float64> 
	{
		private static final long serialVersionUID = 1L;
		
		// Create and initialize the used variable 'x'.
		private final Variable<N> _variable = new Variable.Local<>("x");
		private final List<Variable<N>> _variables = new FastList<>(1);
		{ _variables.add(_variable); }
		
		private final double _min;
		private final double _max;
		private final double _divisor;
		
		public CDF(final Range<N> domain) {
			_min = domain.getMin().doubleValue();
			_max = domain.getMax().doubleValue();
			_divisor = _max - _min;
			assert (_divisor > 0);			
		}
		
		@Override
		public Float64 evaluate() {
			final double x = _variables.get(0).get().doubleValue();
			
			Float64 result = Float64.ZERO;
			if (x < _min) {
				result = Float64.ZERO;
			} else if (x > _max) {
				result = Float64.ONE; 
			} else {
				result = Float64.valueOf((x - _min)/_divisor);
			}
			
			return result;
		}

		@Override
		public List<Variable<N>> getVariables() {
			return _variables;
		}

		@Override
		public Text toText() {
			return Text.valueOf(String.format(
						"P(x) = (x - %1$s)/(%2$s - %1$s)", _min, _max
					));
		}
		
	}


	private final Range<N> _domain;

	/**
	 * Create a new uniform distribution with the given {@code domain}.
	 *
	 * @param domain the domain of the distribution.
	 * @throws NullPointerException if the {@code domain} is {@code null}.
	 */
	public UniformDistribution(final Range<N> domain) {
		_domain = nonNull(domain, "Domain");
	}

	/**
	 * Create a new uniform distribution with the given min and max values.
	 *
	 * @param min the minimum value of the domain.
	 * @param max the maximum value of the domain.
	 * @throws IllegalArgumentException if {@code min >= max}
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
	public UniformDistribution(final N min, final N max) {
		this(new Range<>(min, max));
	}

	@Override
	public Range<N> getDomain() {
		return _domain;
	}

	/**
	 * Return a new PDF object.
	 * 
	 * <p>
	 * <img 
	 *     src="doc-files/uniform-pdf.gif"
	 *     alt="f(x)=\left\{\begin{matrix}
	 *          \frac{1}{max-min} & for & x \in [min, max] \\ 
	 *          0 & & otherwise \\
	 *          \end{matrix}\right."
	 * />
	 * </p>
	 *  
	 */
	@Override
	public Function<N, Float64> pdf() {
		return new PDF<>(_domain);
	}
	
	/**
	 * Return a new CDF object.
	 * 
	 * <p>
	 * <img 
	 *     src="doc-files/uniform-cdf.gif"
	 *     alt="f(x)=\left\{\begin{matrix}
	 *         0 & for & x < min \\ 
	 *         \frac{x-min}{max-min} & for & x \in [min, max] \\
	 *         1 & for & x > max  \\ 
	 *         \end{matrix}\right."
	 * />
	 * </p>
	 *  
	 */
	@Override
	public Function<N, Float64> cdf() {
		return new CDF<>(_domain);
	}
	
	@Override
	public int hashCode() {
		return hashCodeOf(getClass()).and(_domain).value();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		final UniformDistribution<?> dist = (UniformDistribution<?>)obj;
		return eq(_domain, dist._domain);
	}
	
	@Override
	public String toString() {
		return String.format("UniformDistribution[%s]", _domain);
	}

}



