package ma.cdgp.af.generique;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class CodeGenerator {

	public static String rootPath = System.getProperty("user.dir") + "/src/main/java/";

	public static String rootTest = System.getProperty("user.dir") + "/src/test/java/";

	public static void main(String[] args) throws IOException {

		String domaine = "ma.rcar.rsu";

		String entity = "TraceEnvoiRetour";
		String dataSource = "sidacdataSource";
		String query = "fef";

	
		List<Field> properties = new ArrayList<>();
		properties.add(new Field("id", "Long"));
		properties.add(new Field("envoiCdg", "String"));
		properties.add(new Field("retourRsu", "String"));
		properties.add(new Field("dateCdg", "Date"));
		properties.add(new Field("dateRsu", "Date"));
		
		genrateEntityWithPropertiesForEntity(entity, properties, domaine);
//		genrateDtoWithPropertiesForEntity(entity, properties, domaine);
//		genrateRepositoryForEntity(entity, properties, domaine);
//		genrateRepositoryProduitForEntity(entity, domaine, query, dataSource);
//		genrateServiceProduitForEntity(entity, domaine, query, dataSource);
//		genrateControllerForEntity(entity, domaine);
//		genrateTestControllerForEntity(entity, domaine);
	}

	public static void genrateDtoWithPropertiesForEntity(String entity, List<Field> properties, String domaine)
			throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/dtoProp.vm";

		String domainePath = domaine.replace(".", "/");
		String classe = entity + "Dto";
		// TO DO : add fdomaine to path
		String classFile = rootPath + domainePath + "/dto/" + classe + ".java";
		VelocityContext context = new VelocityContext();
		context.put("entity", entity);
		context.put("class", classe);
		context.put("domaine", domaine);
		context.put("properties", properties);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}

	public static void genrateEntityWithPropertiesForEntity(String entity, List<Field> properties, String domaine)
			throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/entityProp.vm";

		String domainePath = domaine.replace(".", "/");
		// TO DO : add fdomaine to path
		String classFile = rootPath + domainePath + "/entity/" + entity + ".java";
		VelocityContext context = new VelocityContext();
		context.put("class", entity);
		context.put("instance", entity.toLowerCase());
		context.put("domaine", domaine);
		context.put("properties", properties);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}

	public static void genrateEntityForEntity(String entity, String domaine) throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/entity.vm";

		String domainePath = domaine.replace(".", "/");
		// TO DO : add fdomaine to path
		String classFile = rootPath + domainePath + "/entity/" + entity + ".java";
		VelocityContext context = new VelocityContext();
		context.put("class", entity);
		context.put("instance", entity.toLowerCase());
		context.put("domaine", domaine);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}

	public static void genrateDtoForEntity(String entity, String domaine) throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/dto.vm";

		String domainePath = domaine.replace(".", "/");
		String classe = entity + "Dto";
		// TO DO : add fdomaine to path
		String classFile = rootPath + domainePath + "/dto/" + classe + ".java";
		VelocityContext context = new VelocityContext();
		context.put("entity", entity);
		context.put("class", classe);
		context.put("domaine", domaine);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}

	public static void genrateRepositoryForEntity(String entity, List<Field> properties, String domaine)
			throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/repository.vm";

		String domainePath = domaine.replace(".", "/");
		String classe = entity + "Repository";
		// TO DO : add fdomaine to path
		String classFile = rootPath + domainePath + "/repository/" + classe + ".java";
		VelocityContext context = new VelocityContext();
		context.put("entity", entity);
		context.put("class", classe);
		context.put("domaine", domaine);
		context.put("properties", properties);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}

	public static void genrateRepositoryForEntity(String entity, String domaine) throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/repository.vm";

		String domainePath = domaine.replace(".", "/");
		String classe = entity + "Repository";
		// TO DO : add fdomaine to path
		String classFile = rootPath + domainePath + "/repository/" + classe + ".java";
		VelocityContext context = new VelocityContext();
		context.put("entity", entity);
		context.put("class", classe);
		context.put("domaine", domaine);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}


	public static void genrateRepositoryProduitForEntity(String entity, String domaine, String query, String dataSource) throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/repositoryProduit.vm";

		String domainePath = domaine.replace(".", "/");
		String classe = entity + "Repository";
		// TO DO : add fdomaine to path
		String classFile = rootPath + domainePath + "/repository/" + classe + ".java";
		VelocityContext context = new VelocityContext();
		context.put("entity", entity);
		context.put("class", classe);
		context.put("domaine", domaine);
		context.put("query", query);
		context.put("dataSource", dataSource);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}

	public static void genrateServiceProduitForEntity(String entity, String domaine, String query, String dataSource) throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/produitService.vm";

		String domainePath = domaine.replace(".", "/");
		String classe = entity + "Service";
		String repo = entity + "Repository";
		// TO DO : add fdomaine to path
		String classFile = rootPath + domainePath + "/service/" + classe + ".java";
		VelocityContext context = new VelocityContext();
		context.put("entity", entity);
		context.put("class", classe);
		context.put("domaine", domaine);
		context.put("repo", repo);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}

	
	public static void genrateControllerForEntity(String entity, String domaine) throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/controller.vm";

		String domainePath = domaine.replace(".", "/");
		String classe = entity + "Controller";
		String dto = entity + "Dto";
		String repository = entity + "Repository";
		// TO DO : add fdomaine to path
		String classFile = rootPath + domainePath + "/controller/" + classe + ".java";
		VelocityContext context = new VelocityContext();
		context.put("entity", entity);
		context.put("repository", repository);
		context.put("instance", entity.toLowerCase());
		context.put("dto", dto);
		context.put("class", classe);
		context.put("domaine", domaine);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}

	public static void genrateTestControllerForEntity(String entity, String domaine) throws IOException {

		String daoTemplatePath = rootPath + "/ma/rcar/rsu/generique/velo/test.vm";

		String domainePath = domaine.replace(".", "/");
		String classe = entity + "ControllerTest";
		String dto = entity + "Dto";
		String repository = entity + "Repository";
		// TO DO : add fdomaine to path
		String classFile = rootTest + domainePath + "/" + classe + ".java";
		VelocityContext context = new VelocityContext();
		context.put("entity", entity);
		context.put("repository", repository);
		context.put("instance", entity.toLowerCase());
		context.put("dto", dto);
		context.put("class", classe);
		context.put("domaine", domaine);
		generateFromTemplate(daoTemplatePath, classFile, context);
	}

	public static void generateFromTemplate(String templateFile, String outputFile, VelocityContext context)
			throws IOException {
		BufferedReader in = new BufferedReader(
				new InputStreamReader(new FileInputStream(templateFile), StandardCharsets.UTF_8));
		StringWriter writer = new StringWriter();
		try {
			Velocity.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Velocity.evaluate(context, writer, "LOG", in);

		Path pathToFile = Paths.get(outputFile);
		if (!Files.exists(pathToFile)) {
			if (!Files.exists(pathToFile.getParent()))
				Files.createDirectories(pathToFile.getParent());
			Files.createFile(pathToFile);
			PrintWriter out = new PrintWriter(outputFile);
			out.println(writer.getBuffer());
			out.close();
		} else {
			System.out.println("File Already Exists: " + pathToFile);
		}
	}
}
