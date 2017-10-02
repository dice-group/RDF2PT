# RDF2PT

Portuguese Verbalizer from RDF triples to NL sentences and summaries.

With the emergent growing of Linked Data, the generation of natural language from Resource Description Framework (RDF) data has gained recently significant attention. However, a few number of these approaches generates natural language in other languages than English. No work has been proposed to generate Brazilian Portuguese texts in the context of RDF. To this end, we present RDF2PT, an approach which verbalizes RDF data to Brazilian Portuguese language. We evaluated RDF2PT in an open questionnaire with 44 nativespeakers divided into experts and non-experts. Our results demonstrate that RDF2PT is able to generate summaries which are similarto natural languages and can be easily understood by humans.

## Results

it can be found at <a href="http://tinyurl.com/ya2hm2vr">Click Here</a>


### A simple example: 

```
dbr:Albert_Einstein 	@dbo:field		dbr:Física
dbr:Albert_Einstein	@dbo:deathPlace		dbr:Princeton
dbr:Albert_Einstein	@dbo:almaMater		dbr:Universidade_de_Zurique
dbr:Albert_Einstein	@rdf:type		dbo:Scientist
dbr:Albert_Einstein	@dbo:knownFor		dbr:Equivalência_massa-energia
dbr:Albert_Einstein	@dbo:award		dbr:Medalha_Max_Planck
dbr:Albert_Einstein	@dbo:doctoralStudent	dbr:Ernst_Gabor_Straus
```
	
**Baseline:** Albert Einstein é cientista, Albert Einstein campo é física, Albert Einstein lugar falecimento Princeton. Albert Einstein ex-instituição é Universidade Zurique, Albert Einstein é conhecido Equivalência massa-energia, Albert Einstein prêmio é Medalha Max Planck, Albert Einstein estudante doutorado é Ernst Gabor Straus.

**Modelo:** Albert Einstein foi um cientista, o campo dele foi a física e ele faleceu no Princeton. Além disso, sua ex-instituição foi a Universidade de Zurique, ele é conhecido pela Equivalência massa-energia, o prêmio dele foi a Medalha Max Planck e o estudante de doutorado dele foi o Ernst Gabor Straus.

**Humano:** Albert Einstein era um cientista, que trabalhava na área de Física. Era conhecido pela fórmula de equivalência entre massa e energia. Formou-se na Universidade de Zurique. Einstein ganhou a medalha Max Planck por seu trabalho. Em Princeton, onde morreu, teve sob sua orientação Ernst Gabor Straus.
