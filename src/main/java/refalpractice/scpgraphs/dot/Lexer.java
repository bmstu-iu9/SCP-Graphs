package refalpractice.scpgraphs.dot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum TYPES {
    LPAREN,
    RPAREN,
    GT,
    LT,
    LET,
    IN,
    ASSIGN,
    SEMICOLON,
    EPSILON,
    ENUMBER,
    ELITER,
    IDENT
}

class Position {
    private int line, pos;

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    Position(int line, int pos) {
        this.line = line;
        this.pos = pos;
    }

    void next_line() {
        this.line += 1;
        this.pos = 1;
    }

    void nextPos(int pos) {
        this.pos += pos;
    }

    public String toString() {
        return "(" + this.line + "," + this.pos + ")";
    }
}

class Match {
    public String value;
    public TYPES type;
    private int line;
    public int pos;

    public Match(Position position, String value, TYPES type) {
        this.line = position.getLine();
        this.pos = position.getPos();
        this.value = value;
        this.type = type;
    }

    @Override
    public String toString() {
        return
                type + " " + "(" + line + "," + pos + ")" +
                        " " + value;

    }
}

class MatchesIterator implements Iterator<Match> {
    List<Match> matches;
    int position;

    public MatchesIterator(List<Match> matches) {
        this.matches = matches;
        position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < matches.size();
    }

    @Override
    public Match next() {
        Match match = matches.get(position);
        position++;
        return match;
    }
}

public class Lexer {
    public static MatchesIterator testMatch(String line, Position pos) {
        List<Match> matches = new ArrayList<>();
        String lParenTemplate = "\\(";
        String rParenTemplate = "\\)";
        String gtTemplate = "&gt;";
        String ltTemplate = "&lt;";
        String letTemplate = "Let";
        String inTemplate = "in";
        String assignTemplate = ":=";
        String semicolonTemplate = ";";
        String epsilonTemplate = "&epsilon;";
        String eNumberTemplate = "e\\.\\d+";
        String eLiterTemplate = "e\\.\\w+";
        String identTemplate = "\\S+";

        String pattern = "(?<lparen>^" + lParenTemplate + ")" +
                "|(?<rparen>^" + rParenTemplate + ")" +
                "|(?<gt>^" + gtTemplate + ")" +
                "|(?<lt>^" + ltTemplate + ")" +
                "|(?<let>^" + letTemplate + ")" +
                "|(?<in>^" + inTemplate + ")" +
                "|(?<assign>^" + assignTemplate + ")" +
                "|(?<semicolon>^" + semicolonTemplate + ")" +
                "|(?<epsilon>^" + epsilonTemplate + ")" +
                "|(?<enumber>^" + eNumberTemplate + ")" +
                "|(?<eliter>^" + eLiterTemplate + ")" +
                "|(?<ident>^" + identTemplate + ")";

        Pattern p = Pattern.compile(pattern);
        Matcher m;

        while (!line.equals("")) {
            m = p.matcher(line);
            if (m.find()) {
                if (m.group("lparen") != null) {
                    String item = m.group("lparen");
                    Match match = new Match(pos, item, TYPES.LPAREN);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());

                } else if (m.group("rparen") != null) {
                    String item = m.group("rparen");
                    Match match = new Match(pos, item, TYPES.RPAREN);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());

                } else if (m.group("gt") != null) {
                    String item = m.group("gt");
                    Match match = new Match(pos, item, TYPES.GT);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());

                } else if (m.group("lt") != null) {
                    String item = m.group("lt");
                    Match match = new Match(pos, item, TYPES.LT);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else if (m.group("let") != null) {
                    String item = m.group("let");
                    Match match = new Match(pos, item, TYPES.LET);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else if (m.group("in") != null) {
                    String item = m.group("in");
                    Match match = new Match(pos, item, TYPES.IN);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else if (m.group("assign") != null) {
                    String item = m.group("assign");
                    Match match = new Match(pos, item, TYPES.ASSIGN);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else if (m.group("semicolon") != null) {
                    String item = m.group("semicolon");
                    Match match = new Match(pos, item, TYPES.SEMICOLON);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else if (m.group("epsilon") != null) {
                    String item = m.group("epsilon");
                    Match match = new Match(pos, item, TYPES.EPSILON);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else if (m.group("enumber") != null) {
                    String item = m.group("enumber");
                    Match match = new Match(pos, item, TYPES.ENUMBER);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else if (m.group("eliter") != null) {
                    String item = m.group("eliter");
                    Match match = new Match(pos, item, TYPES.ELITER);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else if (m.group("ident") != null) {
                    String item = m.group("ident");
                    Match match = new Match(pos, item, TYPES.IDENT);
                    matches.add(match);
                    pos.nextPos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                }
            } else {
                if (line.charAt(0) == '\n') {
                    line = line.substring(1);
                    pos.next_line();
                } else if (Character.isWhitespace(line.charAt(0))) {
                    while (Character.isWhitespace(line.charAt(0))) {
                        line = line.substring(1);
                        pos.nextPos(1);
                    }
                } else {
                    System.out.println("syntax error " + pos.toString());
                    while (!m.find() && !line.equals("")) {
                        line = line.substring(1);
                        pos.nextPos(1);
                        m = p.matcher(line);
                    }
                }
            }
        }
        return new MatchesIterator(matches);
    }
}
