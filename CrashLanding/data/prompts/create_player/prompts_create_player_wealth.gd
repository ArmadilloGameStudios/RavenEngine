[
  {
    "key": "create_player_wealth",
    "text": "Who where your parents",
    "inputs": [
      {
        "text": "local governors",
        "requirement": {
          "trait": "STARTING_LOC",
          "value": 2,
          "comp": "="
        },
        "events": [
          {
            "add_trait": {
              "key": "WEALTH",
              "value": 3
            }
          }
        ]
      }
    ],
    "prompt": "create_player_loc"
  }
]