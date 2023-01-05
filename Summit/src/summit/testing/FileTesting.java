package summit.testing;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import summit.Main;
import summit.util.GameLoader;

public class FileTesting {
    
    public static void main(String asfd[]){

    // System.out.println(GameLoader.getSaves());

    // String rev = "";

    Scanner scanner = new Scanner(System.in);
        
    String testString = scanner.nextLine();

    for (int i = 0; i < testString.length(); i++) {
        System.out.print(testString.charAt(i));
    }

    scanner.close();











    //     System.out.println("CWD is " + Paths.get("").toAbsolutePath().toString());

    //     // initialize FS
    //     Optional<URI> testURI = Optional.ofNullable(FileTesting.class.getResource("/resources/sprites/textsheet.png")).map(url -> {
    //         try {
    //             return url.toURI();
    //         } catch (URISyntaxException e) {
    //             throw new Error(e);
    //         }
    //     });

    //     System.out.println(testURI.get());

    //     if (testURI.isPresent() && testURI.get().getScheme().equals("jar")) {
    //         for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
    //             if (provider.getScheme().equalsIgnoreCase("jar")) {
    //                 try {
    //                     provider.getFileSystem(testURI.get());
    //                 } catch (FileSystemNotFoundException e) {
    //                     try {
    //                         provider.newFileSystem(testURI.get(), Map.of());
    //                     } catch (IOException ioe) {
    //                         throw new Error(ioe);
    //                     }
    //                 }
    //             }
    //         }
    //     }
    }
}
