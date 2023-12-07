import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

public class SchedulingAlgorithm {

  private static double[] processWeights;

  public static Results Run(int runtime, Vector processVector, Results result) {
        int i = 0;
        int comptime = 0;
        int currentProcess = 0;
        int size = processVector.size();
        int completed = 0;
        String resultsFile = "Summary-Processes";

        result.schedulingType = "Batch (Nonpreemptive)";
        result.schedulingName = "Lottery Scheduling";

        initializeProcessWeights(processVector);

        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            sProcess process = (sProcess) processVector.elementAt(currentProcess);
            out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");

            while (comptime < runtime) {
                if (process.cpudone == process.cputime) {
                    completed++;
                    out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");

                    if (completed == size) {
                        result.compuTime = comptime;
                        out.close();
                        return result;
                    }

                    currentProcess = getRandomWeightedIndex(processWeights);
                    process = (sProcess) processVector.elementAt(currentProcess);

                    out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
                }

                process.cpudone++;

                if (process.ioblocking > 0) {
                    process.ionext++;
                }

                comptime++;

                System.out.println("Current Time: " + comptime + " ms");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.compuTime = comptime;
        return result;
    }

  private static void initializeProcessWeights(Vector processVector) {
    int size = processVector.size();
    processWeights = new double[size];

    for (int i = 0; i < size; i++) {
      processWeights[i] = Math.random();
    }
  }

  private static int getRandomWeightedIndex(double[] weights) {
    double totalWeight = 0;

    for (double weight : weights) {
      totalWeight += weight;
    }

    double randomValue = Math.random() * totalWeight;

    for (int i = 0; i < weights.length; i++) {
      randomValue -= weights[i];
      if (randomValue <= 0) {
        return i;
      }
    }

    return -1;
  }
}
