import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    static int cols;
    static int rows;
    static double angle = 0;
    static char[][][] screenBuffer;
    static Thread render = new Thread("renderThread") {
        @Override
        public void run() {
            int fps = 60;
            while (true) {
                long start = System.currentTimeMillis();

                drawScreen();
                angle += 0.03 * (60d / fps);

                try {
                    Thread.sleep((1000 / fps) - (System.currentTimeMillis() - start));
                } catch (Exception ignored) {
                }
            }
        }
    };

    public static int clamp(double x, int min, int max) {
        if (x < min) {
            x = min;
        } else if (x > max) {
            x = max;
        }
        return (int) x;
    }

    public static void initScreen(char[][] screen) {
        for (int row = 0; row < screen.length; row++) {
            for (int col = 0; col < screen[0].length - 1; col++) {
                screen[row][col] = ' ';
            }
            screen[row][cols] = '\n';
        }
    }

    public static void drawScreen() {
        String screenStr = "";
        for (int row = 0; row < screenBuffer[1].length; row++) {
            for (int col = 0; col < screenBuffer[1][0].length; col++) {
                screenStr += screenBuffer[1][row][col];
            }
        }
        screenStr += "\033[H";
        System.out.print(screenStr);
    }

    public static double[][] multiplyMatrices(double[][] matrixA, double[][] matrixB) {
        double[][] finalMatrix = new double[matrixA.length][matrixB[0].length];
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                for (int k = 0; k < matrixA[0].length; k++) {
                    finalMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return finalMatrix;
    }

    public static double[] toArray1D(double[][] array2D) {
        double[] array1D = new double[array2D.length];
        for (int i = 0; i < array1D.length; i++) {
            array1D[i] = array2D[i][0];
        }
        return array1D;
    }

    public static double[][] toArray2D(double[] array1D) {
        double[][] array2D = new double[array1D.length][1];
        for (int i = 0; i < array2D.length; i++) {
            array2D[i][0] = array1D[i];
        }
        return array2D;
    }

    public static double[][] sortVerts(double[][] verts) {
        double[][][] verts3D = new double[verts.length / 4][4][3];
        for (int i = 0; i < verts.length; i += 4) {
            verts3D[i / 4] = new double[][]{verts[i], verts[i + 1], verts[i + 2], verts[i + 3]};
        }
        Arrays.sort(verts3D, new Comparator<double[][]>() {
            @Override
            public int compare(double[][] triangle1, double[][] triangle2) {
                if (((triangle1[0][2] + triangle1[1][2] + triangle1[2][2]) / 3) < ((triangle2[0][2] + triangle2[1][2] + triangle2[2][2]) / 3)) {
                    return 1;
                } else if (((triangle1[0][2] + triangle1[1][2] + triangle1[2][2]) / 3) > ((triangle2[0][2] + triangle2[1][2] + triangle2[2][2]) / 3)) {
                    return -1;
                }
                return 0;
            }
        });

        double[][] finalVerts = new double[verts.length][3];
        for (int i = 0; i < verts3D.length; i++) {
            System.arraycopy(verts3D[i], 0, finalVerts, i * 4, 4);
        }

        return finalVerts;
    }

    public static void render(double[][] verts) {
        render.start();
        while (true) {
            double[][] rotationX = {
                    {1, 0, 0},
                    {0, Math.cos(angle), -Math.sin(angle)},
                    {0, Math.sin(angle), Math.cos(angle)}
            };
            double[][] rotationY = {
                    {Math.cos(angle), 0, Math.sin(angle)},
                    {0, 1, 0},
                    {-Math.sin(angle), 0, Math.cos(angle)}
            };
            double[][] rotationZ = {
                    {Math.cos(angle), -Math.sin(angle), 0},
                    {Math.sin(angle), Math.cos(angle), 0},
                    {0, 0, 1}
            };

            ArrayList<double[]> visibleVerts = new ArrayList<>();
            for (int i = 0; i < verts.length; i += 3) {
                double[] vert1 = toArray1D(multiplyMatrices(rotationX, multiplyMatrices(rotationY, multiplyMatrices(rotationZ, toArray2D(verts[i])))));
                double[] vert2 = toArray1D(multiplyMatrices(rotationX, multiplyMatrices(rotationY, multiplyMatrices(rotationZ, toArray2D(verts[i + 1])))));
                double[] vert3 = toArray1D(multiplyMatrices(rotationX, multiplyMatrices(rotationY, multiplyMatrices(rotationZ, toArray2D(verts[i + 2])))));

                double[] normal = {
                        (((vert2[1] - vert1[1]) * (vert3[2] - vert1[2])) - ((vert2[2] - vert1[2]) * (vert3[1] - vert1[1]))),
                        (((vert2[2] - vert1[2]) * (vert3[0] - vert1[0])) - ((vert2[0] - vert1[0]) * (vert3[2] - vert1[2]))),
                        (((vert2[0] - vert1[0]) * (vert3[1] - vert1[1])) - ((vert2[1] - vert1[1]) * (vert3[0] - vert1[0])))
                };
                double magnitude = Math.sqrt(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
                normal[0] /= magnitude;
                normal[1] /= magnitude;
                normal[2] /= magnitude;

                if (normal[0] * vert1[0] + normal[1] * vert1[1] + normal[2] * (vert1[2] - 10) > 1) {
                    visibleVerts.add(vert1);
                    visibleVerts.add(vert2);
                    visibleVerts.add(vert3);
                    visibleVerts.add(normal);
                }
            }

            double[][] vertsToRender = new double[visibleVerts.size()][3];
            vertsToRender = sortVerts(visibleVerts.toArray(vertsToRender));

            initScreen(screenBuffer[0]);
            for (int i = 0; i < vertsToRender.length; i += 4) {
                fillTriangle(vertsToRender[i], vertsToRender[i + 1], vertsToRender[i + 2], vertsToRender[i + 3]);

            }
            for (int i = 0; i < screenBuffer[0].length; i++) {
                System.arraycopy(screenBuffer[0][i], 0, screenBuffer[1][i], 0, screenBuffer[0][0].length);
            }
        }
    }

    public static void fillTriangle(double[] vert1, double[] vert2, double[] vert3, double[] normal) {
        char[][] tempScreen = new char[screenBuffer[0].length][screenBuffer[0][0].length];
        initScreen(tempScreen);

        double[][] projection = {
                {1, 0, 0},
                {0, 1, 0}
        };

        char[] shadingCharArr = {'.', ',', '-', '~', ':', ';', '=', '!', '*', '#', '$', '@'};
        double[] lightDirection = {0, 0, -1};

        double magnitude = Math.sqrt(lightDirection[0] * lightDirection[0] + lightDirection[1] * lightDirection[1] + lightDirection[2] * lightDirection[2]);
        lightDirection[0] /= magnitude;
        lightDirection[1] /= magnitude;
        lightDirection[2] /= magnitude;

        double dot = normal[0] * lightDirection[0] + normal[1] * lightDirection[1] + normal[2] * lightDirection[2];
        char shadingChar = shadingCharArr[clamp((int) (dot * 12), 0, shadingCharArr.length - 1)];

        vert1 = toArray1D(multiplyMatrices(projection, toArray2D(vert1)));
        vert2 = toArray1D(multiplyMatrices(projection, toArray2D(vert2)));
        vert3 = toArray1D(multiplyMatrices(projection, toArray2D(vert3)));
        drawLine(tempScreen, vert1[0], vert1[1], vert2[0], vert2[1], shadingChar);
        drawLine(tempScreen, vert2[0], vert2[1], vert3[0], vert3[1], shadingChar);
        drawLine(tempScreen, vert3[0], vert3[1], vert1[0], vert1[1], shadingChar);

        // rasterization
        for (int row = 0; row < tempScreen.length; row++) {
            String rowStr = new String(tempScreen[row]);
            for (int rasterize = rowStr.indexOf(shadingChar); rasterize < rowStr.lastIndexOf(shadingChar); rasterize++) {
                tempScreen[row][rasterize] = shadingChar;
            }
        }

        for (int i = 0; i < screenBuffer[0].length; i++) {
            for (int j = 0; j < screenBuffer[0][0].length; j++) {
                if (tempScreen[i][j] == shadingChar) {
                    screenBuffer[0][i][j] = tempScreen[i][j];
                }
            }
        }
    }

    public static void drawTriangle(double[] vert1, double[] vert2, double[] vert3) {
        double[][] projection = {
                {1, 0, 0},
                {0, 1, 0}
        };
        vert1 = toArray1D(multiplyMatrices(projection, toArray2D(vert1)));
        vert2 = toArray1D(multiplyMatrices(projection, toArray2D(vert2)));
        vert3 = toArray1D(multiplyMatrices(projection, toArray2D(vert3)));
        drawLine(screenBuffer[0], vert1[0], vert1[1], vert2[0], vert2[1], '*');
        drawLine(screenBuffer[0], vert2[0], vert2[1], vert3[0], vert3[1], '*');
        drawLine(screenBuffer[0], vert3[0], vert3[1], vert1[0], vert1[1], '*');
    }

    public static void drawLine(char[][] screen, double x1, double y1, double x2, double y2, char ch) {
        x1 = (cols / 2d) + x1 / 2d * cols;
        y1 = (rows / 2d) + y1 / -2d * rows;
        x2 = (cols / 2d) + x2 / 2d * cols;
        y2 = (rows / 2d) + y2 / -2d * rows;

        int d = 0;

        int dx = Math.abs((int) x2 - (int) x1);
        int dy = Math.abs((int) y2 - (int) y1);

        int dx2 = 2 * dx;
        int dy2 = 2 * dy;

        int ix = x1 < x2 ? 1 : -1;
        int iy = y1 < y2 ? 1 : -1;

        int x = (int) x1;
        int y = (int) y1;

        if (dx >= dy) {
            while (true) {
                try {
                    if (screen[y][x] == ' ') {
                        screen[y][x] = ch;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
                if (x == (int) x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
                try {
                    if (screen[y][x] == ' ') {
                        screen[y][x] = ch;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
                if (y == (int) y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
    }

    public static double[][] load(String filename) throws IOException {
        Scanner objFile = new Scanner(new File(filename));
        ArrayList<ArrayList<Double>> verts = new ArrayList<>();
        ArrayList<Integer> faces = new ArrayList<>();

        while (objFile.hasNext()) {
            String line = objFile.nextLine();
            switch (line.charAt(0)) {
                case 'v':
                    String[] vert = line.split(" ");
                    ArrayList<Double> vertAL = new ArrayList<>();
                    vertAL.add(Double.parseDouble(vert[1]));
                    vertAL.add(Double.parseDouble(vert[2]));
                    vertAL.add(Double.parseDouble(vert[3]));
                    verts.add(vertAL);
                    break;
                case 'f':
                    String[] face = line.split(" ");
                    faces.add(Integer.parseInt(face[1]));
                    faces.add(Integer.parseInt(face[2]));
                    faces.add(Integer.parseInt(face[3]));
                    break;
            }
        }

        double[][] finalArray = new double[faces.size()][3];
        for (int i = 0; i < finalArray.length; i++) {
            for (int j = 0; j < 3; j++) {
                finalArray[i][j] = verts.get(faces.get(i) - 1).get(j);
            }
        }

        return finalArray;
    }

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String file = scan.nextLine();
        cols = Integer.parseInt("50");
        rows = Integer.parseInt("50");
        screenBuffer = new char[2][rows][cols + 1];

        initScreen(screenBuffer[0]);
        initScreen(screenBuffer[1]);
        render(load(file));
    }
}
