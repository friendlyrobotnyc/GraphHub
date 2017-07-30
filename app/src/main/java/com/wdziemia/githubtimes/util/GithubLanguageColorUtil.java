package com.wdziemia.githubtimes.util;

import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to get colors for specific languages as found on Github.com. These aren't included in
 * colors.xml as they are hardcoded on the website and are set dynamically within
 * {@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}. This case would change
 * if there was a need to reference them in XML layouts, or change them based on theme.
 */
public class GithubLanguageColorUtil {

    /**
     * Singleton instance
     */
    private static GithubLanguageColorUtil githubLanguageColorUtil = new GithubLanguageColorUtil();

    /**
     * Default color to return incase the language doesnt exist
     */
    static final int DEFAULT_COLOR = 0xFFCCCCCC;

    public static GithubLanguageColorUtil getInstance() {
        return githubLanguageColorUtil;
    }

    private Map<String, Integer> colors = new HashMap<>();

    private GithubLanguageColorUtil() {
        colors.put("Mercury", 0xFFff2b2b);
        colors.put("TypeScript", 0xFF2b7489);
        colors.put("PureBasic", 0xFF5a6986);
        colors.put("Objective-C++", 0xFF6866fb);
        colors.put("Self", 0xFF0579aa);
        colors.put("edn", 0xFFdb5855);
        colors.put("NewLisp", 0xFF87AED7);
        colors.put("Jupyter Notebook", 0xFFDA5B0B);
        colors.put("Rebol", 0xFF358a5b);
        colors.put("Frege", 0xFF00cafe);
        colors.put("Dart", 0xFF00B4AB);
        colors.put("AspectJ", 0xFFa957b0);
        colors.put("Shell", 0xFF89e051);
        colors.put("Web Ontology Language", 0xFF9cc9dd);
        colors.put("xBase", 0xFF403a40);
        colors.put("Eiffel", 0xFF946d57);
        colors.put("Nix", 0xFF7e7eff);
        colors.put("RAML", 0xFF77d9fb);
        colors.put("MTML", 0xFFb7e1f4);
        colors.put("Racket", 0xFF22228f);
        colors.put("Elixir", 0xFF6e4a7e);
        colors.put("SAS", 0xFFB34936);
        colors.put("Agda", 0xFF315665);
        colors.put("wisp", 0xFF7582D1);
        colors.put("D", 0xFFba595e);
        colors.put("Kotlin", 0xFFF18E33);
        colors.put("Opal", 0xFFf7ede0);
        colors.put("Crystal", 0xFF776791);
        colors.put("Objective-C", 0xFF438eff);
        colors.put("ColdFusion CFC", 0xFFed2cd6);
        colors.put("Oz", 0xFFfab738);
        colors.put("Mirah", 0xFFc7a938);
        colors.put("Objective-J", 0xFFff0c5a);
        colors.put("Gosu", 0xFF82937f);
        colors.put("FreeMarker", 0xFF0050b2);
        colors.put("Ruby", 0xFF701516);
        colors.put("Component Pascal", 0xFFb0ce4e);
        colors.put("Arc", 0xFFaa2afe);
        colors.put("Brainfuck", 0xFF2F2530);
        colors.put("Nit", 0xFF009917);
        colors.put("APL", 0xFF5A8164);
        colors.put("Go", 0xFF375eab);
        colors.put("Visual Basic", 0xFF945db7);
        colors.put("PHP", 0xFF4F5D95);
        colors.put("Cirru", 0xFFccccff);
        colors.put("SQF", 0xFF3F3F3F);
        colors.put("Glyph", 0xFFe4cc98);
        colors.put("Java", 0xFFb07219);
        colors.put("MAXScript", 0xFF00a6a6);
        colors.put("Scala", 0xFFDC322F);
        colors.put("Makefile", 0xFF427819);
        colors.put("ColdFusion", 0xFFed2cd6);
        colors.put("Perl", 0xFF0298c3);
        colors.put("Lua", 0xFF000080);
        colors.put("Vue", 0xFF2c3e50);
        colors.put("Verilog", 0xFFb2b7f8);
        colors.put("Factor", 0xFF636746);
        colors.put("Haxe", 0xFFdf7900);
        colors.put("Pure Data", 0xFF91de79);
        colors.put("Forth", 0xFF341708);
        colors.put("Red", 0xFFee0000);
        colors.put("Hy", 0xFF7790B2);
        colors.put("Volt", 0xFF1F1F1F);
        colors.put("LSL", 0xFF3d9970);
        colors.put("eC", 0xFF913960);
        colors.put("CoffeeScript", 0xFF244776);
        colors.put("HTML", 0xFFe44b23);
        colors.put("Lex", 0xFFDBCA00);
        colors.put("API Blueprint", 0xFF2ACCA8);
        colors.put("Swift", 0xFFffac45);
        colors.put("C", 0xFF555555);
        colors.put("AutoHotkey", 0xFF6594b9);
        colors.put("Isabelle", 0xFFFEFE00);
        colors.put("Metal", 0xFF8f14e9);
        colors.put("Clarion", 0xFFdb901e);
        colors.put("JSONiq", 0xFF40d47e);
        colors.put("Boo", 0xFFd4bec1);
        colors.put("AutoIt", 0xFF1C3552);
        colors.put("Clojure", 0xFFdb5855);
        colors.put("Rust", 0xFFdea584);
        colors.put("Prolog", 0xFF74283c);
        colors.put("SourcePawn", 0xFF5c7611);
        colors.put("AMPL", 0xFFE6EFBB);
        colors.put("FORTRAN", 0xFF4d41b1);
        colors.put("ANTLR", 0xFF9DC3FF);
        colors.put("Harbour", 0xFF0e60e3);
        colors.put("Tcl", 0xFFe4cc98);
        colors.put("BlitzMax", 0xFFcd6400);
        colors.put("PigLatin", 0xFFfcd7de);
        colors.put("Lasso", 0xFF999999);
        colors.put("ECL", 0xFF8a1267);
        colors.put("VHDL", 0xFFadb2cb);
        colors.put("Elm", 0xFF60B5CC);
        colors.put("Propeller Spin", 0xFF7fa2a7);
        colors.put("X10", 0xFF4B6BEF);
        colors.put("IDL", 0xFFa3522f);
        colors.put("ATS", 0xFF1ac620);
        colors.put("Ada", 0xFF02f88c);
        colors.put("Unity3D Asset", 0xFFab69a1);
        colors.put("Nu", 0xFFc9df40);
        colors.put("LFE", 0xFF004200);
        colors.put("SuperCollider", 0xFF46390b);
        colors.put("Oxygene", 0xFFcdd0e3);
        colors.put("ASP", 0xFF6a40fd);
        colors.put("Assembly", 0xFF6E4C13);
        colors.put("Gnuplot", 0xFFf0a9f0);
        colors.put("JFlex", 0xFFDBCA00);
        colors.put("NetLinx", 0xFF0aa0ff);
        colors.put("Turing", 0xFF45f715);
        colors.put("Vala", 0xFFfbe5cd);
        colors.put("Processing", 0xFF0096D8);
        colors.put("Arduino", 0xFFbd79d1);
        colors.put("FLUX", 0xFF88ccff);
        colors.put("NetLogo", 0xFFff6375);
        colors.put("C Sharp", 0xFF178600);
        colors.put("CSS", 0xFF563d7c);
        colors.put("Emacs Lisp", 0xFFc065db);
        colors.put("Stan", 0xFFb2011d);
        colors.put("SaltStack", 0xFF646464);
        colors.put("QML", 0xFF44a51c);
        colors.put("Pike", 0xFF005390);
        colors.put("LOLCODE", 0xFFcc9900);
        colors.put("ooc", 0xFFb0b77e);
        colors.put("Handlebars", 0xFF01a9d6);
        colors.put("J", 0xFF9EEDFF);
        colors.put("Mask", 0xFFf97732);
        colors.put("EmberScript", 0xFFFFF4F3);
        colors.put("TeX", 0xFF3D6117);
        colors.put("Nemerle", 0xFF3d3c6e);
        colors.put("KRL", 0xFF28431f);
        colors.put("Ren'Py", 0xFFff7f7f);
        colors.put("Unified Parallel C", 0xFF4e3617);
        colors.put("Golo", 0xFF88562A);
        colors.put("Fancy", 0xFF7b9db4);
        colors.put("OCaml", 0xFF3be133);
        colors.put("Shen", 0xFF120F14);
        colors.put("Pascal", 0xFFb0ce4e);
        colors.put("F0xFF", 0xFFb845fc);
        colors.put("Puppet", 0xFF302B6D);
        colors.put("ActionScript", 0xFF882B0F);
        colors.put("Diff", 0xFF88dddd);
        colors.put("Ragel in Ruby Host", 0xFF9d5200);
        colors.put("Fantom", 0xFFdbded5);
        colors.put("Zephir", 0xFF118f9e);
        colors.put("Click", 0xFFE4E6F3);
        colors.put("Smalltalk", 0xFF596706);
        colors.put("DM", 0xFF447265);
        colors.put("Ioke", 0xFF078193);
        colors.put("PogoScript", 0xFFd80074);
        colors.put("LiveScript", 0xFF499886);
        colors.put("JavaScript", 0xFFf1e05a);
        colors.put("VimL", 0xFF199f4b);
        colors.put("PureScript", 0xFF1D222D);
        colors.put("ABAP", 0xFFE8274B);
        colors.put("Matlab", 0xFFbb92ac);
        colors.put("Slash", 0xFF007eff);
        colors.put("R", 0xFF198ce7);
        colors.put("Erlang", 0xFFB83998);
        colors.put("Pan", 0xFFcc0000);
        colors.put("LookML", 0xFF652B81);
        colors.put("Eagle", 0xFF814C05);
        colors.put("Scheme", 0xFF1e4aec);
        colors.put("PLSQL", 0xFFdad8d8);
        colors.put("Python", 0xFF3572A5);
        colors.put("Max", 0xFFc4a79c);
        colors.put("Common Lisp", 0xFF3fb68b);
        colors.put("Latte", 0xFFA8FF97);
        colors.put("XQuery", 0xFF5232e7);
        colors.put("Omgrofl", 0xFFcabbff);
        colors.put("XC", 0xFF99DA07);
        colors.put("Nimrod", 0xFF37775b);
        colors.put("SystemVerilog", 0xFFDAE1C2);
        colors.put("Chapel", 0xFF8dc63f);
        colors.put("Groovy", 0xFFe69f56);
        colors.put("Dylan", 0xFF6c616e);
        colors.put("E", 0xFFccce35);
        colors.put("Parrot", 0xFFf3ca0a);
        colors.put("Grammatical Framework", 0xFF79aa7a);
        colors.put("Game Maker Language", 0xFF8fb200);
        colors.put("Papyrus", 0xFF6600cc);
        colors.put("NetLinx+ERB", 0xFF747faa);
        colors.put("Clean", 0xFF3F85AF);
        colors.put("Alloy", 0xFF64C800);
        colors.put("Squirrel", 0xFF800000);
        colors.put("PAWN", 0xFFdbb284);
        colors.put("UnrealScript", 0xFFa54c4d);
        colors.put("Standard ML", 0xFFdc566d);
        colors.put("Slim", 0xFFff8f77);
        colors.put("Perl6", 0xFF0000fb);
        colors.put("Julia", 0xFFa270ba);
        colors.put("Haskell", 0xFF29b544);
        colors.put("NCL", 0xFF28431f);
        colors.put("Io", 0xFFa9188d);
        colors.put("Rouge", 0xFFcc0088);
        colors.put("cpp", 0xFFf34b7d);
        colors.put("AGS Script", 0xFFB9D9FF);
        colors.put("Dogescript", 0xFFcca760);
        colors.put("nesC", 0xFF94B0C7);
    }

    /**
     * Return a color for a predetermined set of languages. We return @link{Gith}
     *
     * @param name the name of the language
     * @return a color int
     */
    @ColorInt
    public int getColor(String name) {
        return colors.containsKey(name) ? colors.get(name) : DEFAULT_COLOR;
    }
}
