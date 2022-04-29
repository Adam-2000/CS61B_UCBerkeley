public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p){
        if(p == null){
            return -1;
        }
        return Math.sqrt((p.xxPos - xxPos) * (p.xxPos - xxPos) + (p.yyPos - yyPos) * (p.yyPos - yyPos));
    }

    public double calcForceExertedBy(Planet p) {
        double r = calcDistance(p);
        return G * mass * p.mass / r / r;
    }

    public double calcForceExertedByX(Planet p) {
        double r = calcDistance(p);
        return G * mass * p.mass * (p.xxPos - xxPos) / r / r / r;
    }

    public double calcForceExertedByY(Planet p) {
        double r = calcDistance(p);
        return G * mass * p.mass * (p.yyPos - yyPos) / r / r / r;
    }
    public double calcNetForceExertedByX(Planet[] ps) {
        double res = 0;
        for (Planet p : ps){
            if (p.equals(this)){
                continue;
            }
            res += calcForceExertedByX(p);
        }
        return res;
    }

    public double calcNetForceExertedByY(Planet[] ps) {
        double res = 0;
        for (Planet p : ps){
            if (p.equals(this)){
                continue;
            }
            res += calcForceExertedByY(p);
        }
        return res;
    }

    public void update(double dt, double xF, double yF){
        xxVel += dt * xF / mass;
        yyVel += dt * yF / mass;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }


}
