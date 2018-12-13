/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comm;

import java.io.File;

public class InitialFilePackage {
    final private File[] files;
    
    public InitialFilePackage(String directory){
        files = new File(directory).listFiles();
    }

    public File[] getFiles() {
        return files;
    }
}
