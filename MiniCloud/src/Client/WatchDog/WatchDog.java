package Client.WatchDog;

import Client.DataObservable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;


public class WatchDog extends Thread{

    private DataObservable data;
    private WatchService watcher;
    private WatchKey key;
    private File currentPath;
    private boolean CONTINUE = true;
    
    
    public WatchDog(DataObservable obs)throws WatchDogException {
        data = obs;
        
        try{
            watcher = FileSystems.getDefault().newWatchService();
        }catch(IOException e){
            throw new WatchDogRuntimeException("Error: "+e);
        }
    }
    
    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
    
    private void register(Path dir) throws IOException {
        key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }
    
    public void UpdatePath(String path){
        try{
            register(Paths.get(path));
            this.interrupt();
        }catch(IOException e){
            throw new WatchDogRuntimeException("Error watchdog: "+e);
        }
    }
    
    public void exit(){
        CONTINUE = false;
        this.interrupt();
    }
    
    private File getFile(String filename){
        return new File(currentPath.getAbsolutePath() + File.separator + filename);
    }
    
    @Override
    public void run(){
        WatchKey key;
        
        try {
            register(data.getUploadPath().toPath());
            currentPath = data.getUploadPath();
            
        } catch (IOException ex) {
            try {
                data.logout();
            } catch (IOException ex1) {
            }
        }
        
        for(;CONTINUE;){
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                    continue;
            }
            
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                if (kind == OVERFLOW)
                    continue;

                WatchEvent<Path> ev = cast(event);
                String name = ev.context().getFileName().toString();
                
                
                try{
                    if (ENTRY_CREATE.equals(kind)) {
                        if(!Files.isDirectory(Paths.get(name), LinkOption.NOFOLLOW_LINKS))
                            data.addFileRequest(getFile(name));
                    } else if (ENTRY_DELETE.equals(kind)) {
                        if(!Files.isDirectory(Paths.get(name), LinkOption.NOFOLLOW_LINKS))
                            data.removeFileRequest(getFile(name));
                    } else if (ENTRY_MODIFY.equals(kind)) {
                        if(!Files.isDirectory(Paths.get(name), LinkOption.NOFOLLOW_LINKS))
                            data.updateFileRequest(getFile(name));
                    }
                } catch (IOException ex) {
                    System.out.println("nothing we can do here");
                }
            }
            
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}
