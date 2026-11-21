package cl.nutrivida.catalogo_servicios.config;

import cl.nutrivida.catalogo_servicios.model.Servicio;
import cl.nutrivida.catalogo_servicios.repository.ServicioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initServicios(ServicioRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            List<Servicio> servicios = List.of(
                    Servicio.builder().tipo("Consulta").nombre("Primera consulta nutricional").duracion("50 min").modalidad("Presencial").precio(new BigDecimal("35000")).descripcion("Evaluacion inicial: anamnesis, antropometria completa y diseno del primer plan alimenticio.").imagen("/img/primera consulta.jpg").build(),
                    Servicio.builder().tipo("Consulta").nombre("Control nutricional (seguimiento)").duracion("30 min").modalidad("Presencial").precio(new BigDecimal("25000")).descripcion("Seguimiento mensual: medicion de indicadores y ajuste del plan vigente.").imagen("/img/seguimiento mensual.jpg").build(),
                    Servicio.builder().tipo("Consulta").nombre("Control nutricional quincenal").duracion("30 min").modalidad("Presencial").precio(new BigDecimal("22000")).descripcion("Seguimiento intensivo cada 15 dias. Recomendado en los primeros 2 meses.").imagen("/img/seguimiento quincenal.jpg").build(),
                    Servicio.builder().tipo("Consulta").nombre("Teleconsulta nutricional").duracion("30 min").modalidad("Online (video)").precio(new BigDecimal("20000")).descripcion("Consulta de seguimiento via videollamada. Requiere contar con consulta presencial previa.").imagen("/img/teleconsulta.jpg").build(),
                    Servicio.builder().tipo("Consulta").nombre("Consulta de urgencia / reagendada").duracion("30 min").modalidad("Presencial").precio(new BigDecimal("28000")).descripcion("Para pacientes que requieren atencion fuera de su control habitual.").imagen("/img/consulta urgencia.jpg").build(),
                    Servicio.builder().tipo("Plan especializado").nombre("Plan perdida de peso (1 mes)").duracion("—").modalidad("Presencial").precio(new BigDecimal("65000")).descripcion("Incluye primera consulta + 1 control quincenal + plan alimenticio personalizado + seguimiento por WhatsApp.").imagen("/img/perdida peso 1 mes.jpg").build(),
                    Servicio.builder().tipo("Plan especializado").nombre("Plan perdida de peso (3 meses)").duracion("—").modalidad("Presencial").precio(new BigDecimal("170000")).descripcion("Incluye primera consulta + 5 controles + 3 planes mensuales + seguimiento continuo.").imagen("/img/perdida 3 meses.jpg").build(),
                    Servicio.builder().tipo("Plan especializado").nombre("Plan nutricion deportiva (1 mes)").duracion("—").modalidad("Presencial").precio(new BigDecimal("70000")).descripcion("Para deportistas y personas con actividad fisica frecuente. Calculo de requerimientos energeticos y proteicos.").imagen("/img/nutri deporte 1 mes.jpg").build(),
                    Servicio.builder().tipo("Plan especializado").nombre("Plan control diabetes / hipertension").duracion("—").modalidad("Presencial").precio(new BigDecimal("75000")).descripcion("Plan adaptado para patologias metabolicas. Coordinacion con medico tratante si aplica.").imagen("/img/diabetes.jpg").build(),
                    Servicio.builder().tipo("Plan especializado").nombre("Plan alimentacion vegetariana/vegana").duracion("—").modalidad("Presencial").precio(new BigDecimal("68000")).descripcion("Disenado para garantizar aporte adecuado de proteinas, hierro, vitamina B12 y calcio sin productos animales.").imagen("/img/plan vegano.png").build(),
                    Servicio.builder().tipo("Plan especializado").nombre("Plan alimentacion infantil (2-12 anos)").duracion("—").modalidad("Presencial").precio(new BigDecimal("65000")).descripcion("Evaluacion nutricional pediatrica y diseno de plan adaptado a la etapa de desarrollo del nino.").imagen("/img/infantil.jpg").build(),
                    Servicio.builder().tipo("Evaluacion").nombre("Antropometria completa").duracion("20 min").modalidad("Presencial").precio(new BigDecimal("18000")).descripcion("Peso, talla, IMC, circunferencia de cintura, cadera, brazo y % de grasa corporal con bioimpedanciometria.").imagen("/img/antropometria.jpg").build(),
                    Servicio.builder().tipo("Evaluacion").nombre("Bioimpedanciometria").duracion("15 min").modalidad("Presencial").precio(new BigDecimal("12000")).descripcion("Medicion de composicion corporal: masa grasa, masa muscular, agua corporal y edad metabolica.").imagen("/img/Bioimpedanciometria .jpg").build(),
                    Servicio.builder().tipo("Evaluacion").nombre("Encuesta de habitos alimentarios").duracion("20 min").modalidad("Presencial").precio(new BigDecimal("10000")).descripcion("Analisis del patron alimentario actual. Identificacion de deficit y excesos nutricionales.").imagen("/img/encuesta.jpg").build(),
                    Servicio.builder().tipo("Evaluacion").nombre("Analisis de examenes de laboratorio").duracion("20 min").modalidad("Presencial").precio(new BigDecimal("15000")).descripcion("Interpretacion de hemograma, perfil bioquimico y lipidico en contexto nutricional.").imagen("/img/examenes.jpg").build(),
                    Servicio.builder().tipo("Taller grupal").nombre("Taller de alimentacion saludable").duracion("90 min").modalidad("Presencial (grupo)").precio(new BigDecimal("15000")).descripcion("Max. 10 personas. Conceptos basicos de alimentacion equilibrada y lectura de etiquetas.").imagen("/img/taller saludable.jpg").build(),
                    Servicio.builder().tipo("Taller grupal").nombre("Taller de cocina nutritiva").duracion("120 min").modalidad("Presencial (grupo)").precio(new BigDecimal("20000")).descripcion("Preparacion de recetas saludables. Incluye degustacion. Max. 8 personas.").imagen("/img/taller cocina.jpg").build(),
                    Servicio.builder().tipo("Taller grupal").nombre("Taller nutricion para deportistas").duracion("90 min").modalidad("Presencial (grupo)").precio(new BigDecimal("18000")).descripcion("Hidratacion, nutricion pre y post entrenamiento, suplementacion basica. Max. 12 personas.").imagen("/img/taller deportivo.jpg").build()
            );
            repo.saveAll(servicios);
        };
    }
}