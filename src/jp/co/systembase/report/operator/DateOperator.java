package jp.co.systembase.report.operator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class DateOperator implements IOperator {

	@Override
	public Object exec(Evaluator evaluator, List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 1);
		Object o = evaluator.eval(params.get(0));
		if (o instanceof String) {
			List<String> l = _extract((String)o);
			if (l.size() == 1) {
				String v = l.get(0);
				if (v.length() == 6) {
					Calendar c = Calendar.getInstance();
					c.set(
						_year(Integer.valueOf(v.substring(0, 2))),
						Integer.valueOf(v.substring(2, 4)) - 1,
						Integer.valueOf(v.substring(4, 6)),
						0,0,0);
					return c.getTime();
				}else if(v.length() == 8) {
					Calendar c = Calendar.getInstance();
					c.set(
						_year(Integer.valueOf(v.substring(0, 4))),
						Integer.valueOf(v.substring(4, 6)) - 1,
						Integer.valueOf(v.substring(6, 8)),
						0,0,0);
					return c.getTime();
				}else if(v.length() == 12) {
					Calendar c = Calendar.getInstance();
					c.set(
						_year(Integer.valueOf(v.substring(0, 2))),
						Integer.valueOf(v.substring(2, 4)) - 1,
						Integer.valueOf(v.substring(4, 6)),
						Integer.valueOf(v.substring(6, 8)),
						Integer.valueOf(v.substring(8, 10)),
						Integer.valueOf(v.substring(10, 12)));
					return c.getTime();
				}else if(v.length() == 14) {
					Calendar c = Calendar.getInstance();
					c.set(
						_year(Integer.valueOf(v.substring(0, 4))),
						Integer.valueOf(v.substring(4, 6)) - 1,
						Integer.valueOf(v.substring(6, 8)),
						Integer.valueOf(v.substring(8, 10)),
						Integer.valueOf(v.substring(10, 12)),
						Integer.valueOf(v.substring(12, 14)));
					return c.getTime();
				}
			}
			if (l.size() > 0) {
				Calendar c = Calendar.getInstance();
				c.set(
					_year(Integer.valueOf(l.get(0))),
					l.size() > 1 ? Integer.valueOf(l.get(1)) - 1 : 0,
					l.size() > 2 ? Integer.valueOf(l.get(2)) : 0,
					l.size() > 3 ? Integer.valueOf(l.get(3)) : 1,
					l.size() > 4 ? Integer.valueOf(l.get(4)) : 0,
					l.size() > 5 ? Integer.valueOf(l.get(5)) : 0);
				return c.getTime();
			}
		}else {
			Calendar c = Calendar.getInstance();
			c.set(
				_year(Cast.toInt(evaluator.eval(params.get(0)))),
				params.size() > 1 ? Cast.toInt(evaluator.eval(params.get(1))) - 1 : 0,
				params.size() > 2 ? Cast.toInt(evaluator.eval(params.get(2))) : 1,
				params.size() > 3 ? Cast.toInt(evaluator.eval(params.get(3))) : 0,
				params.size() > 4 ? Cast.toInt(evaluator.eval(params.get(4))) : 0,
				params.size() > 5 ? Cast.toInt(evaluator.eval(params.get(5))) : 0);
			return c.getTime();
		}
		return null;
	}

	private int _year(int y) {
		if (y < 49) {
			return y + 2000;
		}else if (y < 100) {
			return y + 1900;
		}else {
			return y;
		}
	}

	private List<String> _extract(String v){
		List<String> ret = new ArrayList<String>();
		int l = 0;
		for(int i = 0;i < v.length();i++) {
			char c = v.charAt(i);
			if (c < '0' || c > '9') {
				if (i > l) {
					ret.add(v.substring(l, i));
				}
				l = i + 1;
			}
		}
		if (v.length() > l) {
			ret.add(v.substring(l, v.length()));
		}
		return ret;
	}

}
