package com.rikima.ml.myliblinear;

import java.util.ArrayList;
import java.util.List;

import mloss.roc.Curve;

/**
 * Created by mrikitoku on 15/08/31.
 */
public class Evaluation {
    List<Integer> expected;
    List<Integer> actual;
    List<Double> scores;

    private int pp = 0;
    private int pn = 0;
    private int np = 0;
    private int nn = 0;


    public Evaluation() {
        this.expected = new ArrayList<Integer>();
        this.actual   = new ArrayList<Integer>();
        this.scores   = new ArrayList<Double>();
    }

    public void setResult(int expectedY, int actualY) {
        this.expected.add(expectedY);
        this.actual.add(actualY);
    }

    public void setResult(int expectedY, int actualY, double score) {
        this.expected.add(expectedY);
        this.actual.add(actualY);
        double prob = 1.0 / (1 + Math.exp(-10 * score));
        this.scores.add(prob);
    }

    public double roc() {
        Curve analysis = new Curve.PrimitivesBuilder()
                .scores(this.scores)
                .labels(this.expected)
                .build();

        // Calculate AUC ROC
        double area = analysis.rocArea();
        /*
        for (int i = 0; i < this.actual.size(); ++i) {
            System.out.println(String.format("%f\t%d", this.scores.get(i), this.actual.get(i)));
        }
        */
        return area;
    }

    public void printResult(String comment) {
        int size = this.expected.size();
        for (int i = 0; i < size; ++i) {
            int ey = this.expected.get(i);
            int ay = this.actual.get(i);

            if (ey * ay > 0) {
                if (ey > 0) {
                    pp++;
                } else {
                    nn++;
                }
            } else {
                if (ey > 0) {
                    pn++;
                } else {
                    np++;
                }
            }
        }

        System.out.println("\n### " + comment);
        System.out.println(String.format("PP: %d PN: %d NP: %d NN: %d", this.pp, this.pn, this.np, this.nn));
        {//accuracy
            double total = this.pp + this.pn + this.np + this.nn;
            double acc = (this.pp + this.nn) / total;
            System.out.println(String.format("Accuracy: %f", acc));
            System.out.println(String.format("Error rate: %f", 1.0 - acc));
        }
        System.out.println("---");
        {// positive perf
            double recall = (pp) / (double) (pp + pn);
            double prec = (pp) / (double) (pp + np);
            double f1 = 2.0 * recall * prec / (recall + prec);

            System.out.println(String.format("positive Prec: %f", prec));
            System.out.println(String.format("positive Recall: %f", recall));
            System.out.println(String.format("positive F1: %f", f1));
        }
        System.out.println("---");
        {// negative perf
            double recall = (nn) / (double) (nn + pn);
            double prec = (nn) / (double) (nn + np);
            double f1 = 2.0 * recall * prec / (recall + prec);

            System.out.println(String.format("negative Prec: %f", prec));
            System.out.println(String.format("negative Recall: %f", recall));
            System.out.println(String.format("negative F1: %f", f1));
        }
        System.out.println("---");
        {// roc score
            System.out.println(String.format("ROC: %f", this.roc()));
        }

    }
}
