#ifndef __BUILDERFILEWRITER_H
#define __BUILDERFILEWRITER_H

#include "cpgf/gscopedptr.h"

#include <string>
#include <vector>
#include <set>
#include <map>

namespace metagen {


class CppWriter;
class CppItem;
class CppContainer;
class CodeBlock;
class BuilderItem;
class BuilderSection;
class BuilderSectionList;
class BuilderContext;
class Project;

enum FileType {
	ftHeader, ftSource
};

class BuilderFileWriter
{
public:
	typedef std::vector<BuilderItem *> ItemListType;
	
private:
	typedef std::map<const CppContainer *, BuilderSection *> ContainerSectionMapType;
	typedef std::multimap<const CppContainer *, BuilderSection *> ReflectionSectionMultimapType;

public:
	explicit BuilderFileWriter(BuilderContext * builderContext);
	~BuilderFileWriter();
	
	const Project * getProject() const;

	void generateCode();

public:
	CodeBlock * createOperatorWrapperCodeBlock(const CppItem * cppItem);
	CodeBlock * createBitFieldWrapperCodeBlock(const CppItem * cppItem);
	CodeBlock * getParentReflectionCodeBlock(const CppItem * cppItem);
	CodeBlock * getContainerReflectionCodeBlock(const CppContainer * cppContainer);
	
	CodeBlock * getClassWrapperCodeBlock(const CppItem * cppItem);
	CodeBlock * getClassWrapperParentReflectionCodeBlock(const CppItem * cppItem);

public: // auxiliary functions used by BuilderItem's
	std::string getReflectionAction(const std::string & name);
	
	std::string getCreationFunctionName(const CppContainer * cppContainer);
	std::string getCreationFunctionPrototype(const CppContainer * cppContainer);
	
private:
	BuilderSection * getReflectionContainerSection(const CppContainer * cppContainer, const CppItem * payloadItem);
	CodeBlock * getReflectionBodyBlock(CodeBlock * codeBlock);
	void initializeReflectionFunctionOutline(CodeBlock * codeBlock, const CppContainer * cppContainer, int sectionIndex);
	void initializeReflectionFunctionOutline(CodeBlock * codeBlock, const CppContainer * cppContainer,
		const std::string & functionName);
	void createPartialCreationFunction(const CppContainer * cppContainer, int sectionIndex);
	void initializePartialCreationFunction(CodeBlock * codeBlock, const CppContainer * cppContainer, int sectionIndex);

	BuilderSection * getClassWrapperSection(const CppContainer * cppContainer);
	BuilderSection * getClassWrapperReflectionSection(const CppContainer * cppContainer, const CppItem * payloadItem);
	void initializeClassWrapperOutline(CodeBlock * codeBlock, const CppContainer * cppContainer);
	void initializeClassWrapperReflectionOutline(CodeBlock * codeBlock, const CppContainer * cppContainer, int sectionIndex);
	void createPartialClassWrapperCreationFunction(const CppContainer * cppContainer, int sectionIndex);

	void doWriteReflectionFunction(const CppContainer * cppContainer);

	ItemListType * getItemList();
	BuilderSectionList * getSectionList();

private:
	BuilderContext * builderContext;
	ReflectionSectionMultimapType reflectionSectionMap;
	// section map for wrapper class declaration
	ContainerSectionMapType wrapperClassSectionMap;
	// section map for wrapper class reflection function
	ReflectionSectionMultimapType wrapperClassReflectionSectionMap;
};



} // namespace metagen


#endif
