import java.io.*;
import java.util.*;

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
  private String model;
  private int speed;
  private double distance;

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
    int result = this.model.compareTo(airplane.model);
    if (result != 0) {
      return result;
    } else {
      result = Integer.compare(this.speed, airplane.speed);
      if (result != 0) {
        return result;
      } else {
        return Double.compare(this.distance, airplane.distance);
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
    sortAirplanes(typeSort);
    writeFiles(outputFilesDir);
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

  private static void readFile(File file) throws IOException {
    String line;
    BufferedReader br = new BufferedReader(new FileReader(file));
    boolean skipHeader = true;
    while ((line = br.readLine()) != null) {
      if (skipHeader) {
        skipHeader = false;
        continue;
      }
      ArrayList<String> list = new ArrayList<>();
      splitLine(line, list);
      separatePlanes(new Airplane(list.get(1), Integer.parseInt(list.get(2)), Double.parseDouble(list.get(3))));
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

  private static void separatePlanes(Airplane airplane) {
    double speed = airplane.getSpeed();
    if (speed <= 800) {
      SLOW_AIRPLANES.add(airplane);
    } else if (speed >= 801 && speed <= 900) {
      MEDIUM_AIRPLANES.add(airplane);
    } else {
      FAST_AIRPLANES.add(airplane);
    }
  }

  private static void writeFiles(File outputFilesDir) {
    File[] outputFiles = {new File(outputFilesDir, "slowAirplanes.txt"),
        new File(outputFilesDir, "mediumAirplanes.txt"),
        new File(outputFilesDir, "fastAirplanes.txt")};
    try {
      PrintWriter pw = new PrintWriter(new File(outputFilesDir, "slowAirplanes.txt"));
      for (int i = 0; i < SLOW_AIRPLANES.size(); i++) {
        pw.println("" + i + SLOW_AIRPLANES.get(i));
      }
      pw = new PrintWriter(new File(outputFilesDir, "mediumAirplanes.txt"));
      for (int i = 0; i < MEDIUM_AIRPLANES.size(); i++) {
        pw.println("" + i + MEDIUM_AIRPLANES.get(i));
      }
      pw = new PrintWriter(new File(outputFilesDir, "fastAirplanes.txt"));
      for (int i = 0; i < FAST_AIRPLANES.size(); i++) {
        pw.println("" + i + FAST_AIRPLANES.get(i));
      }
      pw.flush();
      pw.close();
    } catch (FileNotFoundException fnfe) {
      System.out.println("file for write not exist!");
    }
  }

  private static void sortAirplanes(String key) {
    switch (key) {
      case "speed": {
        Comparator<Airplane> speedComparator = new Comparator<>() {
          @Override
          public int compare(Airplane m1, Airplane m2) {
            int result = Integer.compare(m1.getSpeed(), m2.getSpeed());
            if (result != 0) {
              return result;
            } else {
              result = m1.compareTo(m2);
              if (result != 0) {
                return result;
              } else {
                return Double.compare(m1.getDistance(), m2.getDistance());
              }
            }
          }
        };
        Collections.sort(SLOW_AIRPLANES, speedComparator);
        Collections.sort(MEDIUM_AIRPLANES, speedComparator);
        Collections.sort(FAST_AIRPLANES, speedComparator);
        break;
      }
      case "distance": {
        Comparator<Airplane> distanceComparator = new Comparator<>() {
          @Override
          public int compare(Airplane m1, Airplane m2) {
            int result = Double.compare(m1.getDistance(), m2.getDistance());
            if (result != 0) {
              return result;
            } else {
              result = m1.getModel().compareTo(m2.getModel());
              if (result != 0) {
                return result;
              } else {
                return Integer.compare(m1.getSpeed(), m2.getSpeed());
              }
            }
          }
        };
        Collections.sort(SLOW_AIRPLANES, distanceComparator);
        Collections.sort(MEDIUM_AIRPLANES, distanceComparator);
        Collections.sort(FAST_AIRPLANES, distanceComparator);
        break;
      }
      case "model":
      default:
        Collections.sort(SLOW_AIRPLANES);
        Collections.sort(MEDIUM_AIRPLANES);
        Collections.sort(FAST_AIRPLANES);
        break;
    }
  }
}
