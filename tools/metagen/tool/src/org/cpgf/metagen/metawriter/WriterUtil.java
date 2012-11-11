package org.cpgf.metagen.metawriter;

import java.util.ArrayList;
import java.util.List;

import org.cpgf.metagen.Config;
import org.cpgf.metagen.Util;
import org.cpgf.metagen.codewriter.CodeWriter;
import org.cpgf.metagen.codewriter.CppWriter;
import org.cpgf.metagen.metadata.CppClass;
import org.cpgf.metagen.metadata.CppField;
import org.cpgf.metagen.metadata.CppMethod;
import org.cpgf.metagen.metadata.Item;
import org.cpgf.metagen.metadata.Parameter;
import org.cpgf.metagen.metadata.TemplateInstance;


public class WriterUtil {

	public static void writeCommentForAutoGeneration(CodeWriter writer) {
		writer.writeLine("// Auto generated file, don't modify.");
		writer.writeLine("");
	}

	public static void writeParamList(CodeWriter writer, List<Parameter> paramList, boolean withName) {
		writer.write(Util.getParameterText(paramList, true, withName));
	}

	public static void writeDefaultParams(CodeWriter writer, List<Parameter> paramList) {
		int index = paramList.size() - 1;

		if(index >= 0 && paramList.get(index).hasDefaultValue()) {
			writer.writeLine("");
			writer.incIndent();

			while(index >= 0 && paramList.get(index).hasDefaultValue()) {
				String value = paramList.get(index).getDefaultValue();
				if(value.equals("NULL") || value.equals("nullptr")) {
					value = "(" + paramList.get(index).getType().getLiteralType() + ")" + value;
				}
				writer.writeLine("._default(copyVariantFromCopyable(" + value + "))");
				--index;
			}

			writer.decIndent();
		}
		writer.writeLine(";");
	}

	public static void defineMetaClass(Config config, CppWriter codeWriter, CppClass cppClass, String varName, String action) {
		List<String> rules = new ArrayList<String>();
		cppClass.getPolicyRules(rules);
		
		String namespace = "\"" + ((config.metaNamespace != null) ? config.metaNamespace : "") + "\"";
		
		if(cppClass.isGlobal()) {
			if(config.metaNamespace != null) {
				codeWriter.writeLine("GDefineMetaNamespace " + varName + " = GDefineMetaNamespace::" + action + "(" + namespace + ");");
			}
			else {
				codeWriter.writeLine("GDefineMetaGlobal " + varName + ";");
			}
		}
		else {
			String prefix = "";
			if(cppClass.getOwner() != null && cppClass.getOwner().isTemplate()) {
				prefix = "typename ";
			}
			String typeName = "GDefineMetaClass<" + prefix + cppClass.getFullQualifiedName();

			typeName = typeName + Util.generateBaseClassList(cppClass);
			
			typeName = typeName + ">";
			String policy = "";
			if(rules != null && rules.size() > 0) {
				policy = "::Policy<MakePolicy<" + Util.joinStringList(", ", rules) + "> >";
			}

			codeWriter.writeLine(typeName +  " " + varName + " = " + typeName + policy + "::" + action + "(\"" + cppClass.getPrimaryName() + "\");");
		}
	}

	private static String normalizeClassName(String name)
	{
		name = name.replaceAll("::", "_");
		name = name.replaceAll(":", "_");
		name = name.replaceAll("<", "_");
		name = name.replaceAll(">", "");
		name = name.replaceAll("\\s", "");

		return name;
	}
	
	public static void createMetaClass(CppWriter codeWriter, CppClass cppClass, String callFunc, List<TemplateInstance> templateInstanceList) {
		List<String> rules = new ArrayList<String>();
		cppClass.getPolicyRules(rules);
		
		if(cppClass.isGlobal()) {
			codeWriter.writeLine("GDefineMetaGlobalDangle _d = GDefineMetaGlobalDangle::dangle();");
			
			codeWriter.writeLine(callFunc + "(0, _d);");
		}
		else {
			String policy = "";
			if(rules != null && rules.size() > 0) {
				policy = "::Policy<MakePolicy<" + Util.joinStringList(", ", rules) + "> >";
			}

			codeWriter.writeLine("GDefineMetaGlobalDangle _d = GDefineMetaGlobalDangle::dangle();");
			if(cppClass.isTemplate()) {
				for(TemplateInstance templateInstance : templateInstanceList) {
					codeWriter.beginBlock();
					
					String typeName = "GDefineMetaClass<" + templateInstance.getFullType();
					typeName = typeName + Util.generateBaseClassList(cppClass, templateInstance);
					typeName = typeName + " >";

					codeWriter.writeLine(typeName +  " _nd = " + typeName + policy + "::declare(\"" + normalizeClassName(templateInstance.getMapName()) + "\");");
					
					codeWriter.writeLine(callFunc + "<" + typeName + ", " + templateInstance.getTemplateType() + " >(0, _nd);");
					codeWriter.writeLine("_d._class(_nd);");

					codeWriter.endBlock();
				}
			}
			else {
				codeWriter.beginBlock();

				String typeName = "GDefineMetaClass<" + cppClass.getLiteralName();
				typeName = typeName + Util.generateBaseClassList(cppClass);
				typeName = typeName + ">";
				codeWriter.writeLine(typeName +  " _nd = " + typeName + policy + "::declare(\"" + cppClass.getPrimaryName() + "\");");
				
				codeWriter.writeLine(callFunc + "(0, _nd);");
				codeWriter.writeLine("_d._class(_nd);");

				codeWriter.endBlock();
			}
		}
	}
	
	public static String getPolicyText(Item item) {
		return getPolicyText(item, true);
	}

	public static String getPolicyText(Item item, boolean prefixWithComma) {
		List<String> rules = new ArrayList<String>();
		
		item.getPolicyRules(rules);
		
		if(rules.size() > 0) {
			String rulesText = "";
			for(String s : rules) {
				if(rulesText.length() > 0) {
					rulesText = rulesText + ", ";
				}
				rulesText = rulesText + "cpgf::" + s;
			}
			return (prefixWithComma ? ", " : "")
					+ "cpgf::MakePolicy<"
					+ rulesText
					+ " >()";
		}
		else {
			return "";
		}
	}
	
	private static String getBitfieldWrapperName(CppField field) {
		return "bItFiEldWrapper_" + Util.normalizeSymbol(field.getQualifiedName());
	}

	public static String getBitfieldWrapperGetterName(CppField field) {
		return getBitfieldWrapperName(field) + "_getter";
	}

	public static String getBitfieldWrapperSetterName(CppField field) {
		return getBitfieldWrapperName(field) + "_setter";
	}

	public static boolean shouldGenerateBitfieldWrapper(Config config, CppField field) {
		return field.isBitField() && Util.allowMetaData(config, field) && !field.getOwner().isTemplate();
	}

	public static String getReflectionAction(String define, String name) {
		return define + ".CPGF_MD_TEMPLATE " + name;
	}
	
	public static String getMethodSuperName(CppMethod method) {
		return "super_" + method.getPrimaryName();
	}
	
	public static void reflectMethod(CppWriter codeWriter, String define, String scopePrefix, CppMethod method, String name, boolean usePrototype) {
		String action = getReflectionAction(define, "_method");

		codeWriter.write(action);
		codeWriter.write("(" + Util.quoteText(name) + ", ");
		if(usePrototype) {
			String typePrefix = scopePrefix;
			if(method.isStatic()) {
				typePrefix = "";
			}
			codeWriter.write("(" + method.getResultType().getLiteralType() + " (" + typePrefix + "*) (");
			writeParamList(codeWriter, method.getParameterList(), false);
			codeWriter.write(")");
			if(!method.isStatic() && method.isConst()) {
				codeWriter.write(" const");
			}
			codeWriter.write(")");
		}
		codeWriter.write("&" + scopePrefix + name + getPolicyText(method) + ")");

		writeDefaultParams(codeWriter, method.getParameterList());
	}
	

}