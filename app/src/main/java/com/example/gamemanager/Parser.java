package com.example.gamemanager;

/*  Intelligently adapted from another project I did which required me to write and read from Json.
*   Could use XML but I personally prefer Json as a format, it's more readable for me and I already know how to use it.
*   Plus I want my GitHub repo to look fancy with me using more than one language instead of just Kotlin :3 */

import com.example.gamemanager.Jsonable;

import java.util.StringTokenizer;
import java.util.List;
import java.util.LinkedList;
import java.lang.StringBuilder;

/* This is a parser made specifically for Volumes */
public class Parser
{
    public static final char TAB = '\t';
    public static final char NLN = '\n';
    public static final char QTM = '\"';
    public static final char SPC = ' ';
    public static final char COL = ':';

    public static final String ENDL = ",\n";
    public static final String JSON_ENTRY_TAB = "\t\t";

    public static final int DONT_USE_TABS = -1;
    public static final int ENTRY_PREDICTED_SIZE = 127;
    public static final int LIBRARY_PREDICTED_SIZE = 2048; // 2kB

    private String toJson(Jsonable v, boolean formatted)
    {
        if (v == null)
        {
            return null;
        }

        String format = formatted ? JSON_ENTRY_TAB : "";
        String ret = format + "{" + NLN;

        ret += v.toJson(formatted ? 1 : DONT_USE_TABS);

        ret += format + "}";

        return ret;
    }

    public String ParseProject(List<Jsonable> lib)
    {
        StringBuilder result = new StringBuilder(LIBRARY_PREDICTED_SIZE);

        result.append("{\n\t\"project\": [\n");

        int i = 0;
        for (Jsonable v : lib)
        {
            result.append(toJson(v, true) + (i != lib.size() - 1 ? ",\n" : "\n"));
            i++;
        }

        result.append("\t]\n}");

        return result.toString();
    }

    static String trim(String s) { return s.replaceAll("^\\s+|\\s+$", ""); }

    // TODO: Remake from scratch, this will be the hardest part.
    private Jsonable toJsonable(String json) throws InvalidJsonEntryException, JsonParsingError
    {
        if (json == null)
        {
            return null;
        }

        //Jsonable ret = new Jsonable(); Implement factory pattern maybe?
        StringTokenizer tokenizer = new StringTokenizer(json, ":\"\t\n", false);

        boolean beganEntry 		= false;
        boolean awaitFieldType 	= true;
        boolean metEnd			= false;
        boolean skipTkn			= false;
        //boolean awaitColon 		= false;		// MIGHT be unnecessary
        String field = "";
        String token = "";
        while (tokenizer.hasMoreTokens())
        {
            if (skipTkn)
            {
                skipTkn = false;
            }
            else
            {
                token = tokenizer.nextToken().trim();
            }

            //System.out.println(token);

            // Entry begin character
            if (token.equals("{"))
            {
                if (beganEntry)
                {
                    throw new InvalidJsonEntryException("Unexpected \'{\' found");
                }
                beganEntry = true;
                continue;
            }

            // We don't care about spaces
            if (token.equals(" ") || token.equals(""))
            {
                continue;
            }

            // Met the end of the Json entry
            if (token.replace(" ", "").equals("}"))
            {
                break;
            }

            // Met the comma = analyzing next field
            if (token.equals(","))
            {
                awaitFieldType = true;
                //awaitColon = false;
                continue;
            }

            // If we're awaiting a field type, we flag that we expect a colon next and
            if (awaitFieldType)
            {
                field = token;
                awaitFieldType = false;
                continue;
            }

			/*// Met a colon
			if (awaitColon && token.replace(" ", "").equals(":"))
			{
				awaitColon = false;
				continue;
			}*/

            // Memorize value
            field = field.toLowerCase();
            if (token.endsWith("}"))
            {
                System.out.println("token includes closed bracket");
                metEnd = true;
                token = token.substring(0, token.length() - 1).trim();
            }
            else if (token.startsWith("}"))
            {
                System.out.println("Token starts with closed bracket");
                token = token.substring(1);
                skipTkn = true;
            }
            switch (field)
            {
                case "title":
                    if (ret.GetTitle() == null)
                    {
                        ret.SetTitle(token);
                    }
                    else
                    {
                        throw new InvalidJsonEntryException("Json entry defined the same field (title) more than once");
                    }
                    break;
                case "author":
                    if (ret.GetAuthor() == null)
                    {
                        ret.SetAuthor(token);
                    }
                    else
                    {
                        throw new InvalidJsonEntryException("Json entry defined the same field (author) more than once");
                    }
                    break;
                case "genre":
                    if (ret.GetGenre() == null)
                    {
                        ret.SetGenre(token);
                    }
                    else
                    {
                        throw new InvalidJsonEntryException("Json entry defined the same field (genre) more than once");
                    }
                    break;
                case "isbn":
                    if (ret.GetISBN() == null)
                    {
                        ret.SetISBN(token);
                    }
                    else
                    {
                        throw new InvalidJsonEntryException("Json entry defined the same field (ISBN) more than once");
                    }
                    break;
                case "rating":
                    if (ret.GetRating() == Volume.UNINITIALIZED_RATING)
                    {
                        if (token.length() >= 2)
                        {
                            throw new InvalidJsonEntryException("Invalid rating value found!");
                        }

                        if (!Character.isDigit(token.charAt(0)))
                        {
                            throw new InvalidJsonEntryException("Rating value should be a number");
                        }

                        byte val = (byte) Character.getNumericValue(token.charAt(0));
                        if (val < 0 || val > 5)
                        {
                            throw new InvalidJsonEntryException("Rating must be a value between 0 and 5 (inclusive)");
                        }

                        ret.SetRating(val);
                    }
                    else
                    {
                        throw new InvalidJsonEntryException("Json entry defined the same field (rating) more than once");
                    }
                    break;
                case "state":
                    if (ret.GetState() == null)
                    {
                        token = trim(token.toLowerCase());

                        switch (token)
                        {
                            case "read":
                                ret.SetState(ReadingState.READ);
                                break;
                            case "reading":
                                ret.SetState(ReadingState.READING);
                                break;
                            case "to_be_read": case "to be read":
                            ret.SetState(ReadingState.TO_BE_READ);
                            break;
                            default:
                                // Reading state is not a "volume defining" information, and it'e easy to modify,
                                // so it's not a huge deal if it doesn't initialize properly sometimes
                                ret.SetState(ReadingState.TO_BE_READ);
                                System.out.println("ILLEGAL READING STATE READ: " + token);
                                break;
                        }
                    }
                    else
                    {
                        throw new InvalidJsonEntryException("Json entry defined the same field (state) more than once");
                    }
                    break;
                default:
                    throw new JsonParsingError("Unexpected field found->" + field);
            }

            if (metEnd)
            {
                break;
            }
        }

        if (ret.GetState() == null)
        {
            System.out.println("NO STATE FOUND, DEFAULTING TO TO_BE_READ.");
            ret.SetState(ReadingState.TO_BE_READ);
        }

        return ret;
    }

    public List<Volume> ParseJson(String json) throws
            InvalidJsonSequenceException,
            InvalidJsonEntryException,
            JsonParsingError
    {
        LinkedList<Volume> ret = new LinkedList<Volume>();

        boolean inSequence 	= false;
        boolean inEntry		= false;
        StringBuilder entryBuilder = new StringBuilder(ENTRY_PREDICTED_SIZE);
        String entryJson;
        for (char c : json.toCharArray())
        {
            // Sequence begin character
            if (c == '[')
            {
                if (inSequence)
                {
                    throw new InvalidJsonSequenceException("Found multiple begin sequence characters.");
                }
                inSequence = true;
                continue;
            }

            // Sequence end character
            if (c == ']')
            {
                if (!inSequence)
                {
                    throw new InvalidJsonSequenceException("Met an end of sequence character when the sequence was never opened");
                }
                if (inEntry)
                {
                    throw new InvalidJsonSequenceException("Sequence ended in the middle of an entry.");
                }
                inSequence = false;
                break;
            }

            // Entry begin character
            if (c == '{')
            {
                if (!inSequence)
                {
                    System.out.println("We're not in a sequence, entry begin character can be ignored.");
                    continue;
                }
                inEntry = true;
            }

            if (inEntry)
            {
                entryBuilder.append(c);
            }

            // Entry end character
            if (c == '}')
            {
                if (!inSequence)
                {
                    System.out.println("We're not in a sequence, entry end character can be ignored.");
                    continue;
                }
                inEntry = false;

                // This part could -and should- be done with multithreading,
                // but because of a lack of time I'll add it last if I can
                entryJson = entryBuilder.toString();
                //System.out.println("Json entry: " + entryJson);
                ret.add(toVolume(entryJson));
                entryBuilder.delete(0, entryBuilder.length());
            }
        }

        return ret;
    }
}

class InvalidJsonEntryException extends Exception
{
    public InvalidJsonEntryException(String msg)
    {
        super(msg);
    }
}

class JsonParsingError extends Exception
{
    public JsonParsingError(String msg)
    {
        super(msg);
    }
}

class InvalidJsonSequenceException extends Exception
{
    public InvalidJsonSequenceException(String msg)
    {
        super(msg);
    }
}
