[
  {
    "name": "start_school",
    "text": "What did you focus on at school?",
    "inputs": [
      {
        "text": "making many friends",
        "events": [
          {
            "increment_trait": {
              "name": "PERSONALITY_CHARM",
              "value": 1,
              "max": 3
            }
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "being the top of your class",
        "requirement": {
          "trait": "PERSONALITY_MOTIVATION",
          "value": 2,
          "comp": ">="
        },
        "events": [
          {
            "increment_traits": [
              {
                "name": "PERSONALITY_MOTIVATION",
                "value": 1
              },
              {
                "name": "SKILL_EDUCATION",
                "value": 1,
                "max": 2
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "learning music",
        "events": [
          {
            "increment_traits": [
              {
                "name": "SKILL_MUSIC",
                "value": 1,
                "max": 2
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "athletics",
        "events": [
          {
            "increment_traits": [
              {
                "name": "SKILL_TOUGH",
                "value": 1,
                "max": 2
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "cheat your way though",
        "events": [
          {
            "increment_traits": [
              {
                "name": "SKILL_DECEIT",
                "value": 1,
                "max": 2
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "bully the other kids",
        "events": [
          {
            "increment_traits": [
              {
                "name": "SKILL_MELEE",
                "value": 1,
                "max": 2
              }
            ],
            "decrement_traits": [
              {
                "name": "PERSONALITY_CHARM",
                "value": 1
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "flunk out",
        "requirement": {
          "trait": "AGE",
          "value": 3,
          "comp": ">="
        },
        "events": [
          {
            "decrement_traits": [
              {
                "name": "SKILL_EDUCATION",
                "value": 1
              },
              {
                "name": "PERSONALITY_MOTIVATION",
                "value": 1
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      }
    ]
  },
  {
    "name": "start_university",
    "text": "What did you do at university?",
    "inputs": [
      {
        "text": "worked on forming connections",
        "events": [
          {
            "increment_trait": {
              "name": "PERSONALITY_CHARM",
              "value": 1
            }
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "being the top of your class",
        "requirement": {
          "trait": "PERSONALITY_MOTIVATION",
          "value": 2,
          "comp": ">="
        },
        "events": [
          {
            "increment_traits": [
              {
                "name": "PERSONALITY_MOTIVATION",
                "value": 1
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "study engineering",
        "events": [
          {
            "increment_traits": [
              {
                "name": "SKILL_CRAFTING",
                "value": 2
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "cheat your way though",
        "requirement": {
          "trait": "SKILL_DECEIT",
          "value": 1,
          "comp": ">="
        },
        "events": [
          {
            "increment_traits": [
              {
                "name": "SKILL_DECEIT",
                "value": 1
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "flunk out",
        "events": [
          {
            "decrement_traits": [
              {
                "name": "SKILL_EDUCATION",
                "value": 1
              },
              {
                "name": "PERSONALITY_MOTIVATION",
                "value": 1
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      }
    ]
  }
]