package responseRecords;

import java.util.ArrayList;

public record ListGamesResponse(ArrayList<ListGameInfo> games) {
}
