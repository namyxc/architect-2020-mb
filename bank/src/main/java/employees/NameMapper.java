package employees;

import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface NameMapper {

    NameDto nameToNameDto(Name name);
    Name nameDtoToName(NameDto name);
}
