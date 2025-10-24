.SILENT:

JC = javac
JAVA = java

SRCDIR = src
BINDIR = bin
MAIN_CLASS = principal.Main
JFLAGS = -g -d $(BINDIR)

SOURCES = $(shell find $(SRCDIR) -name "*.java")

.DEFAULT_GOAL := run

run: compile
	$(JAVA) -cp $(BINDIR) $(MAIN_CLASS)

compile: $(BINDIR) $(SOURCES)
	$(JC) $(JFLAGS) -sourcepath $(SRCDIR) $(SOURCES)

$(BINDIR):
	@mkdir -p $(BINDIR)

clean:
	@echo "Limpando diretório $(BINDIR)..."
	@rm -rf $(BINDIR)

.PHONY: run compile clean