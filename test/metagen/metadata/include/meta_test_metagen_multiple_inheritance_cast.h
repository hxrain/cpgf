// Auto generated file, don't modify.

#ifndef __META_TEST_METAGEN_MULTIPLE_INHERITANCE_CAST_H
#define __META_TEST_METAGEN_MULTIPLE_INHERITANCE_CAST_H


#include "cpgf/gmetadefine.h"
#include "cpgf/metadata/gmetadataconfig.h"
#include "cpgf/metadata/private/gmetadata_header.h"
#include "cpgf/gmetapolicy.h"




namespace meta_test { 


template <typename D>
void buildMetaClass_Global_metagen_multiple_inheritance_cast(const cpgf::GMetaDataConfigFlags & config, D _d)
{
    (void)config; (void)_d; (void)_d;
    using namespace cpgf;
    
    _d.CPGF_MD_TEMPLATE _method("getMultipleInheritanceCastD", (MultipleInheritanceCastD * (*) ())&getMultipleInheritanceCastD);
    _d.CPGF_MD_TEMPLATE _method("checkMultipleInheritanceCastDAsR", (bool (*) (MultipleInheritanceCastR *))&checkMultipleInheritanceCastDAsR);
}


template <typename D>
void buildMetaClass_MultipleInheritanceCastA(const cpgf::GMetaDataConfigFlags & config, D _d)
{
    (void)config; (void)_d; (void)_d;
    using namespace cpgf;
    
    _d.CPGF_MD_TEMPLATE _field("a", &D::ClassType::a);
}


template <typename D>
void buildMetaClass_MultipleInheritanceCastB(const cpgf::GMetaDataConfigFlags & config, D _d)
{
    (void)config; (void)_d; (void)_d;
    using namespace cpgf;
    
    _d.CPGF_MD_TEMPLATE _field("c", &D::ClassType::c);
    _d.CPGF_MD_TEMPLATE _method("c1", &D::ClassType::c1);
}


template <typename D>
void buildMetaClass_MultipleInheritanceCastC(const cpgf::GMetaDataConfigFlags & config, D _d)
{
    (void)config; (void)_d; (void)_d;
    using namespace cpgf;
    
    _d.CPGF_MD_TEMPLATE _field("d", &D::ClassType::d);
}


template <typename D>
void buildMetaClass_MultipleInheritanceCastD(const cpgf::GMetaDataConfigFlags & config, D _d)
{
    (void)config; (void)_d; (void)_d;
    using namespace cpgf;
    
    _d.CPGF_MD_TEMPLATE _field("e", &D::ClassType::e);
}


template <typename D>
void buildMetaClass_MultipleInheritanceCastR(const cpgf::GMetaDataConfigFlags & config, D _d)
{
    (void)config; (void)_d; (void)_d;
    using namespace cpgf;
    
    _d.CPGF_MD_TEMPLATE _field("r", &D::ClassType::r);
}


} // namespace meta_test




#include "cpgf/metadata/private/gmetadata_footer.h"


#endif