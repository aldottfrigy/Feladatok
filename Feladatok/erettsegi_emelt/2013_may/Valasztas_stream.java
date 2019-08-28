import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Valasztas_stream {
	
	public static void main(String[] args) throws IOException{
		var szavazatok = Files.lines(Paths.get("szavazatok.txt")).map(Szavazat::new).toArray(Szavazat[]::new);
		var kepviselokSzama = Arrays.stream(szavazatok).map(k -> k.nev).distinct().count();
		
		System.out.println("2.Feladat: V�laszt�son indult k�pvisel�k sz�ma: " + kepviselokSzama);
		
		try(var input = new Scanner(System.in)){
			System.out.println("3.Feladat: �rd be 1 k�pvisel� Els� nev�t");
			String firstName = input.nextLine();
			System.out.println("�rd be 1 k�pvisel� M�sodik nev�t");
			String lastName = input.nextLine();
			
			Arrays.stream(szavazatok)
				  .filter(k -> k.nev.equals(firstName + " " + lastName))
				  .findAny()
				  .ifPresentOrElse(l -> System.out.println("A jel�ltre j�tt szavazatok sz�ma: " + l.szavazottSzam), 
						  			() -> System.out.println("Nem volt ilyen jel�lt!"));
		}
		
		var szavazatokSzama = Arrays.stream(szavazatok).mapToInt(k -> k.szavazottSzam).sum();
		System.out.printf("4.Feladat: A v�laszt�son " + szavazatokSzama + 
						" szavaztak, ami sz�zal�kban az �sszesnek a %.2f %%-a.\n", ((float)szavazatokSzama / 12_345) * 100);
		
		System.out.println("5.Feladat");
		Arrays.stream(szavazatok)
			  .collect(Collectors.groupingBy(k -> k.part, Collectors.summingInt(l -> l.szavazottSzam)))
			  .forEach((key, value) -> System.out.printf(key + "-ra szavazottak sz�ma: %.2f %%.\n", ((float)value / szavazatokSzama) * 100));
		
		System.out.println("6.Feladat");
		var legtobbSzavazat = Arrays.stream(szavazatok)
									.max(Comparator.comparingInt(k -> k.szavazottSzam))
								    .get().szavazottSzam;
		
		Arrays.stream(szavazatok)
			  .filter(k -> k.szavazottSzam == legtobbSzavazat)
			  .forEach(k -> System.out.println(k.nev + ", t�mogat� p�rt neve: " + k.part));
		
		try(var output = new PrintWriter("kepviselok.txt")){
			Arrays.stream(szavazatok)
				  .collect(Collectors.groupingBy(k -> k.part, Collectors.maxBy(Comparator.comparingInt(l -> l.szavazottSzam))))
				  .entrySet().stream()
				  .sorted(Comparator.comparingInt(l -> l.getValue().get().kerSzam))
				  .forEach(entry -> output.println(entry.getValue().get().kerSzam + " " + entry.getValue().get().nev + " " + entry.getKey()));
		}
	}
	
	public static class Szavazat{
		public final int kerSzam, szavazottSzam;
		public final String nev, part;
		
		public Szavazat(String data) {
			var split = data.split(" ");
			
			kerSzam = Integer.parseInt(split[0]);
			szavazottSzam = Integer.parseInt(split[1]);
			nev = split[2] + " " + split[3];
			part = split[4].equals("-") ? "F�ggetlen" : split[4];
		}
	}
}