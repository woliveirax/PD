package Client.WatchDog;

import Client.DataObservable;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WatchDog extends Thread{

    private DataObservable data;
    private WatchService watcher;
    private WatchKey key;
    private boolean CONTINUE = true;
    
    
    public WatchDog(DataObservable obs)throws WatchDogException {
        data = obs;
        
        try{
            watcher = FileSystems.getDefault().newWatchService();
        }catch(IOException e){
            throw new WatchDogRuntimeException("Error: "+e);
        }
    }
    
    public WatchDog(){//TODO: Remove this constructor
        try{
            watcher = FileSystems.getDefault().newWatchService();
            register(Paths.get("C:\\Users\\Olympus\\Desktop\\"));
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
            throw new WatchDogRuntimeException("Error: "+e);//TODO: try to remove this.
        }
    }
    
    public void exit(){
        CONTINUE = false;
        this.interrupt();
    }
    
    @Override
    public void run(){
        WatchKey key;
        
        for(;CONTINUE;){
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                if(CONTINUE)
                    continue;
                else
                    return;
            }
            
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                if (kind == OVERFLOW)
                    continue;

                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                
                System.out.println(
                  "Event kind:" + event.kind() 
                    + ". File affected: " + event.context() + ".");
                
                
                try{
                    if (ENTRY_CREATE.equals(kind)) {
                    data.addFileRequest(ev.context().toFile());
                    } else if (ENTRY_DELETE.equals(kind)) {
                        data.removeFileRequest(ev.context().toFile());
                    } else if (ENTRY_MODIFY.equals(kind)) {
                        data.updateFileRequest(ev.context().toFile());
                    }
                } catch (IOException ex) {
                    System.out.println("nothing we can do here");
                }
            }
            
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) { //TODO: Figure a way to handle if this happens
                break;
            }
        }
    }
}
