#!/usr/bin/python

input = ARGF.read
leaderboard = Leaderboard.new

# Keep each non-empty line.
commands = []
input.each_line do |line|
  commands.push(line) unless !line.nil? && line.strip.empty? 
end

# Parse each line using regex and execute the corresponding class method.
commands.each do |command|

  # https://stackoverflow.com/questions/8162444/ruby-regex-extracting-words
  args = command.scan(/\s*("([^"]+)"|\w+)\s*/).map { |match| match[1].nil? ? match[0] : match[1] }

  r = case [args[0], args.size]
    when ["AddPlayer", 3]         then leaderboard.add_player(*args)
    when ["AddGame", 3]           then leaderboard.add_game(*args)
    when ["AddVictory", 5]        then leaderboard.add_victory(*args)
    when ["Plays", 4]             then leaderboard.plays(*args)
    when ["AddFriends", 3]        then leaderboard.add_friends(*args)
    when ["WinVictory", 4]        then leaderboard.win_victory(*args)
    when ["FriendsWhoPlay", 3]    then leaderboard.friends_who_play(*args)
    when ["ComparePlayers", 4]    then leaderboard.compare_players(*args)
    when ["SummarizePlayer", 2]   then leaderboard.summarize_player(*args)
    when ["SummarizeGame", 2]     then leaderboard.summarize_game(*args)
    when ["SummarizeVictory", 3]  then leaderboard.summarize_victory(*args)
    when ["VictoryRanking", 1]    then leaderboard.victory_ranking
    else printf("Invalid command: #{command}")
  end
end