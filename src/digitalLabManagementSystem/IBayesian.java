package digitalLabManagementSystem;

import BayesianNetworks.DiscreteVariable;
import BayesianNetworks.BayesNet;
import java.net.URL;
import BayesianNetworks.DiscreteFunction;
import BayesianInferences.Inference;
import BayesianInferences.Ordering;
import BayesianInferences.Explanation;
import java.io.InputStream;
import BayesianInferences.Expectation;
import java.util.Vector;
import BayesianInferences.BucketTree;

public interface IBayesian {
  public BayesNet bayesNet();
  public BayesNet bayesNet(String n_n, int n_v, int n_f);
  public BayesNet bayesNet(String n_n, Vector p);
  public BayesNet bayesNet(BayesNet bn);
  public BayesNet bayesNet(String s);
  public BayesNet bayesNet(InputStream istream);
  public BayesNet bayesNet(URL context, String spec);
  public BayesNet bayesNet(URL url);
  public BucketTree bucketTree(Ordering ord);
  public BucketTree bucketTree(Ordering ord, boolean dpc);
  public DiscreteFunction discreteFunction(int n_vb, int i);
  public DiscreteFunction discreteFunction(DiscreteVariable[] pvs, double[] v);
  public DiscreteVariable discreteVariable();
  public DiscreteVariable discreteVariable(String n_vb);
  public DiscreteVariable discreteVariable(String vb, int vi, String[] v);
  public DiscreteVariable discreteVariable(DiscreteVariable dv);
  public Expectation expectation(BayesNet b_n, boolean dpc);
  public Explanation explanation(BayesNet bn);
  public Inference inference(BayesNet b_n, boolean dpc);
  public Ordering ordering(BayesNet b_n, String objective, int ot);
  public Ordering ordering(BayesNet b_n, String[] or);
  public Ordering ordering(BayesNet b_n, String objective, int ds, int ot);
  public Ordering ordering(BayesNet b_n, String[] or, int ds);
}
