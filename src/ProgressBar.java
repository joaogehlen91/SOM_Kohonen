import java.util.concurrent.TimeUnit;
import java.util.Collections;
public class ProgressBar {
  private long startTime;
  public ProgressBar(long st){
    this.startTime = st;  
  }  
  public void printProgress(long total, long current) {
      long eta = current == 0 ? 0 : 
          (total - current) * (System.currentTimeMillis() - this.startTime) / current;

      String etaHms = current == 0 ? "N/A" : 
              String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
                      TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
                      TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

      StringBuilder string = new StringBuilder(80);   
      int percent = (int) (current * 100 / total);
      string
          .append('\r')
          .append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
          .append(String.format(" %d%% [", percent))
          .append(String.join("", Collections.nCopies(percent, "=")))
          .append('>')
          .append(String.join("", Collections.nCopies(100 - percent, " ")))
          .append(']')
          .append(String.join("", Collections.nCopies((int) (Math.log10(total)) - (int) (Math.log10(current)), " ")))
          .append(String.format("Epoca %d/%d, ETA: %s", current, total, etaHms));

      System.out.print(string);
  }
}