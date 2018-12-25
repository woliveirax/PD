/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comm;

import java.io.Serializable;

public class FileData implements Serializable {
    private String name;
    private long size;

    public FileData(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "FileData{" + "name=" + name + ", size=" + size + '}';
    }
}
