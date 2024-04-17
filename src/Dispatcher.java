import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Dispatcher {
  public static void main(String[] args) {
    File inputFiles = new File("C:\\Users\\ivang\\Desktop\\JavaLess\\CourseProject\\inputFiles");
    File outputFiles = new File("C:\\Users\\ivang\\Desktop\\JavaLess\\CourseProject\\outputFiles");
    inputFiles.mkdir();
    outputFiles.mkdir();

    File f1 = new File(inputFiles, "file1.txt");
    File f2 = new File(inputFiles, "file2.txt");
    File f3 = new File(inputFiles, "file3.txt");

    Controller.handleFiles(outputFiles, f1, f2, f3);
    System.out.println("files read and wrote");
    //////////////////////////////////////////////////
    System.out.println(Controller.SLOW_AIRPLANES);
    System.out.println(Controller.MEDIUM_AIRPLANES);
    System.out.println(Controller.FAST_AIRPLANES);
  }
}

class Airplane implements Comparable<Airplane> {
  private final String model;
  private final int speed;
  private final double distance;

  Airplane(String model, int speed, double distance) {
    this.model = model;
    this.speed = speed;
    this.distance = distance;
  }
  public String getModel() {
    return model;
  }
  public int getSpeed() {
    return speed;
  }
  public double getDistance() {
    return distance;
  }

  @Override
  public String toString() {
    return this.model + " " + this.speed + " " + this.distance;
  }

  @Override
  public int compareTo(Airplane airplane) {
    int result = Double.compare(this.speed, airplane.speed);
    if (result != 0) {
      return result;
    } else {
      result = this.model.compareTo(airplane.model);
      if (result != 0) {
        return result;
      } else {
        return this.model.compareTo(airplane.model);
      }
    }
  }
}

class Controller {
  public static final ArrayList<Airplane> SLOW_AIRPLANES = new ArrayList<>();
  public static final ArrayList<Airplane> MEDIUM_AIRPLANES = new ArrayList<>();
  public static final ArrayList<Airplane> FAST_AIRPLANES = new ArrayList<>();

  public static void handleFiles(File outputFilesDir, File... files) {
    String typeSort = inputSortType();
    for (File file : files) {
      try {
        readFile(file);
      } catch (FileNotFoundException fnfe) {
        System.out.println("file for read with path " + file.getPath() + " not exist!");
      } catch (IOException ioe) {
        System.out.println("exception is thrown: " + ioe.getMessage());
      }
    }
//        writeFiles(outputFilesDir, getSortedBottles(typeSort));  // !!!
  }

  private static String inputSortType() {
    System.out.println("""
        Select sort type:
        1 - By Model
        2 - By Speed
        3 - By Distance
        Any other input - Default sorting
        Enter:\s""");
    Scanner input = new Scanner(System.in);
    String str = "";
    try {
      int type = input.nextInt();
      if (type == 1) {
        str = "model";
      } else if (type == 2) {
        str = "speed";
      } else if (type == 3) {
        str = "distance";
      }
    } catch (Exception e) {
      str = "default";
    }
    System.out.println("You chose " + str + " type of sort.");
    return str;
  }

  private static void readFile(File file) throws IOException{
    String line;
    BufferedReader br = new BufferedReader(new FileReader(file));
    boolean skipHeader = true;
    while ((line = br.readLine()) != null){
      if (skipHeader) {
        skipHeader = false;
        continue;
      }
      ArrayList<String> list = new ArrayList<>();
      splitLine(line, list);
      sortPlanes(new Airplane(list.get(1), Integer.parseInt(list.get(2)), Double.parseDouble(list.get(3))));
    }
    br.close();
  }

  private static void splitLine(String line, ArrayList<String> list) { // !!!
    String[] array = line.split(" ");
    for (String str : array) {
      if (str != "") {
        list.add(str);
      }
    }
  }

  private static void sortPlanes(Airplane airplane) {
    double speed = airplane.getSpeed();
    if (speed <= 800) {
      SLOW_AIRPLANES.add(airplane);
    } else if (speed >= 801 && speed <= 900) {
      MEDIUM_AIRPLANES.add(airplane);
    } else {
      FAST_AIRPLANES.add(airplane);
    }
  }
}
