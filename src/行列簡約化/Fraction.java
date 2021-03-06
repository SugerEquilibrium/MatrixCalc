package 行列簡約化;

public class Fraction {

	int bunshi;
	int bunbo;
	int defaultBunbo;

	public Fraction(int bunshi, int bunbo) {
		this.bunshi = bunshi;
		this.bunbo = bunbo;	//0の場合例外処理を追加
		if(this.bunbo == 0) {
			throw new IllegalArgumentException("div by zero");
		}
		this.correctFraction();
		defaultBunbo = 1;
	}
	public Fraction() {
		this(0, 1);
		defaultBunbo = 1;
	}

	public int getBunbo() {
		return this.bunbo;
	}

	public int getBunshi() {
		return this.bunshi;
	}

	public String toString() {
		this.correctFraction();
		String f;
		if(this.bunshi == 0) {
			f = "0";
		}else if(this.bunbo == 1) {
			f = this.bunshi + "";
		}else {
			f = this.bunshi + "/" + this.bunbo;
		}
		return f;
	}

	public void print() {
		System.out.println(this.toString());
	}


	//分数を約分し、
	//分母が負の場合分母分子両方に-1をかける
	public void correctFraction() {
		this.yakubun();
		if(this.bunbo < 0) {
			this.bunbo *= -1;
			this.bunshi *= -1;
		}
	}

	//約分します
	public void yakubun() {
		if(this.bunbo < 0) {
			this.bunbo *= -1;
			this.bunshi *= -1;
		}
		int s = saidaikouyakusu(this.bunshi, this.bunbo);
		this.bunshi /= s;
		this.bunbo /= s;
	}

	//ユークリッド互除法を用いて最大公約数を求めそれをint型で返します
	public static int saidaikouyakusu(int i1, int i2) {
		int s = 1;
		if(i1 == 0 || i2 == 0) {
			;
		}else {
			int d = Math.max(i1, i2);
			s = Math.min(i1, i2);
			int r = d % s;
			for (; r != 0;) {
				d = s;
				s = r;
				r = d % r;
			}
		}
		return s;
	}

	//thisをfに通分します どちらかに0が代入された場合はそのまま
	public void tsubun(Fraction f) {
		this.correctFraction();

		if(this.bunshi == 0) {
			this.bunbo = f.bunbo;
		}else if( f.bunshi == 0) {
			f.bunbo = this.bunbo;
		}else {
			int saishoukoubaisu = this.bunbo * f.getBunbo() / saidaikouyakusu(this.bunbo, f.bunbo);

			this.bunshi = this.bunshi * saishoukoubaisu / this.bunbo;
			this.bunbo = this.bunbo * saishoukoubaisu / this.bunbo;
		}
	}

	//引数の分数を掛けます
	public void multiply(Fraction f) {
		this.correctFraction();
		this.bunshi *= f.bunshi;
		this.bunbo *= f.bunbo;
		this.correctFraction();
	}
	public static Fraction multiply(Fraction F1, Fraction F2) {
		Fraction ans = new Fraction();
		Fraction f1 = F1.copy();
		Fraction f2 = F2.copy();
		f1.correctFraction();
		f2.correctFraction();
		ans.bunshi = f1.bunshi * f2.bunshi;
		ans.bunbo = f1.bunbo * f2.bunbo;
		ans.correctFraction();
		return ans;
	}

	//分数同士の割り算
	public void div(Fraction f) {
		if(f.bunshi == 0) {
			throw new IllegalArgumentException("div by zero");
		}
		this.correctFraction();

		//自分自身で割った時、参照先が同じインスタンスを変更してしまうのを防ぐ
		Fraction temp = new Fraction(f.bunshi, f.bunbo);
		temp.reverse();
		this.multiply(temp);
		this.correctFraction();
	}
	//f1/f2を返します
	public static Fraction div(Fraction F1, Fraction F2) {
		Fraction ans = new Fraction();
		Fraction f1 = F1.copy();
		Fraction f2 = F2.copy();
		f1.correctFraction();
		f2.correctFraction();
		f2.reverse();
		ans.bunshi = f1.bunshi * f2.bunshi;
		ans.bunbo = f1.bunbo * f2.bunbo;
		ans.correctFraction();
		return ans;
	}

	//逆数
	public void reverse() {
		this.correctFraction();
		//分母0を回避
		if(this.bunshi == 0) {
			throw new IllegalArgumentException("div by zero");
		}
		int temp = this.bunshi;
		this.bunshi = this.bunbo;
		this.bunbo = temp;
		this.correctFraction();
	}

	//引数の分数をたします
	public void add(Fraction f) {
		this.correctFraction();
		this.tsubun(f);
		f.tsubun(this);
		this.bunshi += f.bunshi;
		this.correctFraction();
	}
	//f1+f2を返します
	public static Fraction add(Fraction F1, Fraction F2) {
		Fraction ans = new Fraction();
		Fraction f1 = F1.copy();
		Fraction f2 = F2.copy();
		f1.correctFraction();
		f2.correctFraction();
		f1.tsubun(f2);
		f2.tsubun(f1);
		ans.bunbo = f1.bunbo;
		ans.bunshi = f1.bunshi + f2.bunshi;
		ans.correctFraction();
		return ans;
	}

	//引数の分数を引きます
	public void delta(Fraction f) {
		this.correctFraction();
		this.tsubun(f);
		f.tsubun(this);
		this.bunshi -= f.bunshi;
		this.correctFraction();
	}
	//f1-f2の分数を返します
	public static Fraction delta(Fraction F1, Fraction F2) {
		Fraction ans = new Fraction();
		Fraction f1 = F1.copy();
		Fraction f2 = F2.copy();
		f1.correctFraction();
		f2.correctFraction();
		f1.tsubun(f2);
		f2.tsubun(f1);
		ans.bunbo = f1.bunbo;
		ans.bunshi = f1.bunshi - f2.bunshi;
		ans.correctFraction();
		return ans;
	}

	//引数の分数より大きければtrueを返す
	public boolean compare(Fraction f) {
		boolean bigger = false;
		Fraction temp = new Fraction(this.bunshi, this.bunbo);
		temp.tsubun(f);
		f.tsubun(temp);
		if(temp.getBunshi() > f.getBunshi()) {
			bigger = true;
		}
		return bigger;
	}

	//引数の分数と等しければtrueを返します
	public boolean equals(Fraction f) {
		boolean equal = false;
		if(this.bunshi == f.bunshi && this.bunbo == f.bunbo) {
			equal = true;
		}
		return equal;
	}

	//この単項式をコピーした単項式を返します
	public Fraction copy() {
		Fraction copy = new Fraction();
		copy.bunshi = this.bunshi;
		copy.bunbo = this.bunbo;
		return copy;
	}

	//String型で渡された分数をFraction型に変換します
	// -2/21
	public static Fraction convert(String s) {
		int slash = s.indexOf('/');
		int bunshi;
		int bunbo;
		if(slash < 0) {
			if(s.equals("-")) {
				s = "-1";
			}
			bunshi = Integer.parseInt(s);
			bunbo = 1;
		}else {
			if(s.equals("-")) {
				s = "-1";
			}
			bunshi = Integer.parseInt(s.substring(0, slash));
			bunbo = Integer.parseInt(s.substring(slash+1, s.length()));
		}
		Fraction f = new Fraction(bunshi, bunbo);
		return f;
	}

}
