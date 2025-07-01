/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package math;

/**
 *
 * @author brand
 */

public class MathOp {
    private int id;      // Unique identifier for the operation
    private int x;       // First operand
    private int y;       // Second operand
    private int result;  // Result of the operation

    // Default constructor (required for deserialization)
    public MathOp() {
    }

    // Parameterized constructor
    public MathOp(int id, int x, int y, int result) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.result = result;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MathOp{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", result=" + result +
                '}';
    }
}
