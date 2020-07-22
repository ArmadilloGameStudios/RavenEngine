[
  {
    "name": "start_gang",
    "text": "What did you do in the gang as a <0#STATUS_GANG:title>?",
    "inputs": [
      {
        "text": "transport black market goods",
        "requirement": {
          "trait": "STATUS_GANG",
          "value": 2,
          "comp": "<="
        },
        "events": [
          {
            "increment_traits": [
              {
                "name": "SKILL_DECEIT",
                "value": 1
              },
              {
                "name": "STATUS_GANG",
                "value": 1,
                "max": 3
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "enforce the gangs boundaries",
        "requirement": {
          "all": [
            {
              "trait": "AGE",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "STATUS_GANG",
              "value": 3,
              "comp": "<="
            }
          ]
        },
        "events": [
          {
            "increment_traits": [
              {
                "name": "SKILL_MELEE",
                "value": 1
              },
              {
                "name": "STATUS_GANG",
                "value": 1,
                "max": 3
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "recruit new members",
        "requirement": {
          "all": [
            {
              "trait": "AGE",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "STATUS_GANG",
              "value": 3,
              "comp": "<="
            },
            {
              "trait": "PERSONALITY_CHARM",
              "value": 2,
              "comp": ">="
            }
          ]
        },
        "events": [
          {
            "increment_traits": [
              {
                "name": "PERSONALITY_CHARM",
                "value": 1
              },
              {
                "name": "STATUS_GANG",
                "value": 1,
                "max": 3
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "violently take over the gang",
        "requirement": {
          "all": [
            {
              "trait": "AGE",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "STATUS_GANG",
              "value": 3,
              "comp": "="
            },
            {
              "trait": "SKILL_TOUGH",
              "value": 2,
              "comp": ">="
            }
          ]
        },
        "events": [
          {
            "add_trait": {
              "name": "STATUS_GANG",
              "value": 4
            },
            "increment_traits": [
              {
                "name": "WEALTH",
                "value": 1,
                "max": 2
              }
            ]
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "work your way to the top",
        "requirement": {
          "all": [
            {
              "trait": "AGE",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "STATUS_GANG",
              "value": 3,
              "comp": "="
            },
            {
              "trait": "PERSONALITY_CHARM",
              "value": 3,
              "comp": ">="
            },
            {
              "trait": "PERSONALITY_MOTIVATION",
              "value": 2,
              "comp": ">="
            }
          ]
        },
        "events": [
          {
            "add_trait": {
              "name": "STATUS_GANG",
              "value": 4
            },
            "increment_traits": [
              {
                "name": "WEALTH",
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
    "name": "end_gang",
    "text": "How did you leave the gang?",
    "post_input": {
      "remove_trait": {
        "name": "STATUS_GANG"
      }
    },
    "inputs": [
      {
        "text": "talked them into letting you leave",
        "requirement": {
          "trait": "PERSONALITY_CHARM",
          "value": 3,
          "comp": ">="
        },
        "events": [
          {
            "increment_traits": [
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
        "text": "turn them over to the authorities",
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
        "text": "steal their goods and run",
        "requirement": {
          "trait": "SKILL_DECEIT",
          "value": 1,
          "comp": ">="
        },
        "events": [
          {
            "increment_traits": [
              {
                "name": "WEALTH",
                "value": 1
              },
              {
                "name": "SKILL_DECEIT",
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
    "name": "start_kidnap",
    "text": "how was your kidnapping resolved",
    "inputs": [
      {
        "text": "your ransom was paid",
        "prompt": "choices_create_player"
      },
      {
        "text": "you escaped your ransomers",
        "events": [
          {
            "increment_trait": {
              "name": "SKILL_DECEIT",
              "value": 1
            }
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "your ransomers abandoned you",
        "events": [
          {
            "add_trait": {
              "name": "WEALTH",
              "value": 1
            }
          }
        ],
        "prompt": "choices_create_player"
      },
      {
        "text": "you joined your captors",
        "events": [
          {
            "add_traits": [
              {
                "name": "WEALTH",
                "value": 1
              },
              {
                "name": "STATUS_GANG",
                "value": 1
              }
            ],
            "increment_trait": {
              "name": "SKILL_MELEE",
              "value": 1
            }
          }
        ],
        "prompt": "choices_create_player"
      }
    ]
  }
]