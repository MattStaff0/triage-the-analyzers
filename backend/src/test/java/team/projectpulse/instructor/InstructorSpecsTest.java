package team.projectpulse.instructor;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * The search terms are lowercased in Java and compared with MySQL's LOWER() of the column, so the Java side must
 * lowercase the same way on every server. Under a Turkish default locale, "I" lowercases to the dotless "ı",
 * which MySQL's utf8mb4_0900_ai_ci does not match to "i".
 */
@ExtendWith(MockitoExtension.class)
class InstructorSpecsTest {

    @Mock
    Root<Instructor> root;

    @Mock
    CriteriaQuery<?> query;

    @Mock
    CriteriaBuilder criteriaBuilder;

    @Mock
    Path<String> column;

    @Mock
    Expression<String> loweredColumn;

    Locale defaultLocale;

    @BeforeEach
    void setUp() {
        this.defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.forLanguageTag("tr"));
    }

    @AfterEach
    void tearDown() {
        Locale.setDefault(this.defaultLocale);
    }

    @Test
    void hasFirstNameLowercasesIndependentOfDefaultLocale() {
        given(this.root.<String>get("firstName")).willReturn(this.column);
        given(this.criteriaBuilder.lower(this.column)).willReturn(this.loweredColumn);

        InstructorSpecs.hasFirstName("Isaac").toPredicate(this.root, this.query, this.criteriaBuilder);

        then(this.criteriaBuilder).should().equal(this.loweredColumn, "isaac");
    }

    @Test
    void hasLastNameLowercasesIndependentOfDefaultLocale() {
        given(this.root.<String>get("lastName")).willReturn(this.column);
        given(this.criteriaBuilder.lower(this.column)).willReturn(this.loweredColumn);

        InstructorSpecs.hasLastName("WILLIAMS").toPredicate(this.root, this.query, this.criteriaBuilder);

        then(this.criteriaBuilder).should().equal(this.loweredColumn, "williams");
    }

    @Test
    void hasEmailLowercasesIndependentOfDefaultLocale() {
        given(this.root.<String>get("email")).willReturn(this.column);
        given(this.criteriaBuilder.lower(this.column)).willReturn(this.loweredColumn);

        InstructorSpecs.hasEmail("B.WEI@ABC.EDU").toPredicate(this.root, this.query, this.criteriaBuilder);

        then(this.criteriaBuilder).should().equal(this.loweredColumn, "b.wei@abc.edu");
    }

}
